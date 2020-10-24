package com.github.lehjr.mpalib.util.string;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import com.github.lehjr.mpalib.util.capabilities.inventory.modechanging.IModeChangingItem;
import com.github.lehjr.mpalib.util.capabilities.inventory.modularitem.IModularItem;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.util.energy.ElectricAdapterManager;
import com.github.lehjr.mpalib.util.energy.adapter.IElectricAdapter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.client.util.InputMappings;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class AdditionalInfo {
    /**
     * Adds information to the item's tooltip when 'getting' it.
     *
     * @param stack            The itemstack to get the tooltip for
     * @param worldIn          The world (unused)
     * @param currentTipList   A list of strings containing the existing tooltip. When
     *                         passed, it will just contain the id of the item;
     *                         enchantments and lore are
     *                         appended afterwards.
     * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
     *                         their settings.
     */
    public static void addInformation(@Nonnull ItemStack stack, World worldIn, List currentTipList, ITooltipFlag advancedToolTips) {
        if (worldIn == null) {
            return;
        }

//        // TODO: remove enchantment labels.
//        if (currentTipList.contains(I18n.format("silkTouch"))) {
//            currentTipList.remove(I18n.format("silkTouch"));
//        }

        // Modular Item Check
        stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
            // base class
            if(iItemHandler instanceof IModularItem) {
                // Mode changing item such as power fist
                if (iItemHandler instanceof IModeChangingItem) {
                    ItemStack activeModule = ((IModeChangingItem) iItemHandler).getActiveModule();
                    if (!activeModule.isEmpty()) {

                        // IFormattableTextComponent
                        // TranslationTextComponent
                        IFormattableTextComponent localizedName = (IFormattableTextComponent) activeModule.getDisplayName();
                        currentTipList.add(
                                new TranslationTextComponent("tooltip.mpalib.mode")
//                                        .appendString(" ")
                                        .append(new StringTextComponent(" "))
                                        .append(localizedName.setStyle(Style.EMPTY.setFormatting(TextFormatting.RED))));
                    } else {
                        currentTipList.add(new TranslationTextComponent("tooltip.mpalib.changeModes"));
                    }
                }

                if (doAdditionalInfo()) {
                    List<ITextComponent> installed = new ArrayList<>();
                    Map<ITextComponent, FluidInfo> fluids = new HashMap<>();

                    if(iItemHandler instanceof IModularItem) {
                        for (ItemStack module : ((IModularItem) iItemHandler).getInstalledModules()) {
                            installed.add(((IFormattableTextComponent)module.getDisplayName()).setStyle(Style.EMPTY.setFormatting((TextFormatting.LIGHT_PURPLE))));

                            // check mpodule for fluid
                            module.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY).ifPresent(fluidHandler ->{
                                int numTanks = fluidHandler.getTanks();

                                for(int i=0; i < numTanks; i++) {
                                    FluidStack fluidStack = fluidHandler.getFluidInTank(i);
                                    if (fluidStack.isEmpty()) {
                                        continue;
                                    }
                                    int capacity = fluidHandler.getTankCapacity(i);

                                    ITextComponent fluidName = fluidHandler.getFluidInTank(i).getDisplayName();
                                    FluidInfo fluidInfo = fluids.getOrDefault(fluidName, new FluidInfo(fluidName)).addAmmount(fluidStack.getAmount()).addMax(capacity);
                                    fluids.put(fluidName, fluidInfo);
                                }
                            });
                        }
                    }

                    if (fluids.size() > 0) {
                        for(FluidInfo info : fluids.values()) {
                            currentTipList.add(info.getOutput());
                        }
                    }

                    if (installed.size() == 0) {
                        String message = I18n.format("tooltip.mpalib.noModules");
                        currentTipList.addAll(StringUtils.wrapStringToLength(message, 30));
                    } else {
                        currentTipList.add(new TranslationTextComponent("tooltip.mpalib.installedModules"));
                        currentTipList.addAll(installed);
                    }
                } else {
                    currentTipList.add(additionalInfoInstructions());
                }
            }
        });

        stack.getCapability(PowerModuleCapability.POWER_MODULE).ifPresent(iPowerModule -> {
            if (doAdditionalInfo()) {
                String description = I18n.format( stack.getTranslationKey() + ".desc");
                currentTipList.addAll(StringUtils.wrapStringToLength(description, 30));
            } else {
                currentTipList.add(additionalInfoInstructions());
            }
        });

        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(stack, true);
        if (adapter != null) {
            currentTipList.add(new StringTextComponent(I18n.format(MPALibConstants.TOOLTIP_ENERGY,
                    StringUtils.formatNumberShort(adapter.getEnergyStored()),
                    StringUtils.formatNumberShort(adapter.getMaxEnergyStored())))
                    .setStyle(Style.EMPTY.setFormatting(TextFormatting.AQUA).setItalic(true)));
        }
    }

    static class FluidInfo {
        TranslationTextComponent displayName;
        int currentAmount=0;
        int maxAmount=0;

        FluidInfo(ITextComponent displayName) {
            this.displayName = (TranslationTextComponent)displayName;
        }

        public ITextComponent getDisplayName() {
            return displayName;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public int getCurrentAmount() {
            return currentAmount;
        }

        public FluidInfo addMax(int maxAmountIn) {
            maxAmount += maxAmountIn;
            return this;
        }

        public FluidInfo addAmmount(int currentAmountIn) {
            currentAmount += currentAmountIn;
            return this;
        }

        public ITextComponent getOutput() {
            return displayName.append(new StringTextComponent(": ")).append(new StringTextComponent(new StringBuilder(currentAmount).append("/").append(maxAmount).toString()))
                    .setStyle(Style.EMPTY.setFormatting(TextFormatting.DARK_AQUA).setItalic(true));
        }
    }

    public static ITextComponent additionalInfoInstructions() {
        return new TranslationTextComponent("tooltip.mpalib.pressShift")
                .setStyle(Style.EMPTY.setFormatting(TextFormatting.GRAY).setItalic(true));
    }

    public static List<ITextComponent> getItemInstalledModules(@Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(iItemHandler -> {
            List<ITextComponent> moduleNames = new ArrayList<>();

            if(iItemHandler instanceof IModularItem) {
                for (ItemStack module : ((IModularItem) iItemHandler).getInstalledModules()) {
                    moduleNames.add(((IFormattableTextComponent) module.getDisplayName()).setStyle(Style.EMPTY.setFormatting(TextFormatting.LIGHT_PURPLE)));
                }
            }
            return moduleNames;
        }).orElse(new ArrayList<>());
    }

    public static boolean doAdditionalInfo() {
        return InputMappings.isKeyDown(Minecraft.getInstance().getMainWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT);
    }
}
