package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import com.github.lehjr.mpalib.basemod.ModObjects;
import com.github.lehjr.mpalib.config.MPALibSettings;
import com.github.lehjr.mpalib.util.capabilities.energy.ForgeEnergyModuleWrapper;
import com.github.lehjr.mpalib.util.capabilities.energy.IEnergyWrapper;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.*;
import com.github.lehjr.mpalib.util.string.AdditionalInfo;
import com.github.lehjr.mpalib.util.string.StringUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class Battery extends Item {
    protected int maxEnergy;
    protected int maxTransfer;

    public Battery(int maxEnergy, int maxTransfer) {
        super(new Item.Properties()
                .maxStackSize(1)
                .group(ModObjects.creativeTab)
                .defaultMaxDamage(-1)
                .setNoRepair());
        this.maxEnergy = maxEnergy;
        this.maxTransfer = maxTransfer;
    }

    @Override
    public void addInformation(ItemStack itemStack, @Nullable World worldIn, List<ITextComponent> tooltips, ITooltipFlag flagIn) {
        if (worldIn != null) {
            super.addInformation(itemStack, worldIn, tooltips, flagIn);
            AdditionalInfo.addInformation(itemStack, worldIn, tooltips, flagIn);

//            itemStack.getCapability(CapabilityEnergy.ENERGY).ifPresent(iEnergyStorage -> {
//                tooltips.add(new StringTextComponent(I18n.format(MPALibConstants.TOOLTIP_ENERGY,
//                        StringUtils.formatNumberShort(iEnergyStorage.getEnergyStored()),
//                        StringUtils.formatNumberShort(iEnergyStorage.getMaxEnergyStored()))));
//            });
//            tooltips.add(new TranslationTextComponent(getTranslationKey() +".desc"));
        }
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundNBT nbt) {
        return new CapProvider(stack);
    }

    public class CapProvider implements ICapabilityProvider {
        ItemStack module;
        IPowerModule moduleCap;
        IEnergyWrapper energyStorage;

        public CapProvider(@Nonnull ItemStack module) {
            this.module = module;
            this.moduleCap = new PowerModule(module, EnumModuleCategory.ENERGY_STORAGE, EnumModuleTarget.ALLITEMS, MPALibSettings::getModuleConfig);
            this.moduleCap.addBaseProperty(MPALibConstants.MAX_ENERGY, maxEnergy, "FE");
            this.moduleCap.addBaseProperty(MPALibConstants.MAX_TRAMSFER, maxTransfer, "FE");
            this.energyStorage = new ForgeEnergyModuleWrapper(
                    module,
                    (int)moduleCap.applyPropertyModifiers(MPALibConstants.MAX_ENERGY),
                    (int)moduleCap.applyPropertyModifiers(MPALibConstants.MAX_TRAMSFER)
            );
        }

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            if (cap == CapabilityEnergy.ENERGY) {
                energyStorage.updateFromNBT();
                return CapabilityEnergy.ENERGY.orEmpty(cap, LazyOptional.of(() -> energyStorage));
            }
            if (cap == PowerModuleCapability.POWER_MODULE) {
                return PowerModuleCapability.POWER_MODULE.orEmpty(cap, LazyOptional.of(() -> moduleCap));
            }
            return LazyOptional.empty();
        }
    }

    @Override
    public void fillItemGroup(@Nonnull ItemGroup group, @Nonnull NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        if (isInGroup(group)) {
            ItemStack out = new ItemStack(this);
            CapProvider provider = new CapProvider(out);
            int maxEnergy = (int) provider.moduleCap.applyPropertyModifiers(MPALibConstants.MAX_ENERGY);
            provider.energyStorage.receiveEnergy(maxEnergy, false);
            items.add(out);
        }
    }

    @Override
    public boolean showDurabilityBar(final ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map( energyCap-> energyCap.getMaxEnergyStored() > 0).orElse(false);
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY)
                .map( energyCap-> 1 - energyCap.getEnergyStored() / (double) energyCap.getMaxEnergyStored()).orElse(1D);
    }
}
