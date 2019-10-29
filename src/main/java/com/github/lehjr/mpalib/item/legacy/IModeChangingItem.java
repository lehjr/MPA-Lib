package com.github.lehjr.mpalib.item.legacy;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.module.legacy.IEnchantmentModule;
import com.github.lehjr.mpalib.module.legacy.IModuleManager;
import com.github.lehjr.mpalib.module.legacy.IPowerModule;
import com.github.lehjr.mpalib.module.legacy.IRightClickModule;
import com.github.lehjr.mpalib.nbt.NBTUtils;
import com.github.lehjr.mpalib.network.MPALibPackets;
import com.github.lehjr.mpalib.network.legacypackets.LegacyModeChangeRequestPacket;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:11 PM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 11/1/16.
 */
public interface IModeChangingItem extends IModularItem {
    @SideOnly(Side.CLIENT)
    @Nullable
    default TextureAtlasSprite getModeIcon(String mode, @Nonnull ItemStack stack, EntityPlayer player) {
        if (stack.getItem() instanceof IModularItem) {
            IModuleManager moduleManager = ((IModularItem) stack.getItem()).getModuleManager();
            IPowerModule module = moduleManager.getModule(mode);
            if (module != null)
                return module.getIcon(stack);
        }
        return null;
    }

    // modes are IRightClick modules
    default List<String> getValidModes(@Nonnull ItemStack stack) {
        List<String> modes = new LinkedList<>();
        if (!(stack.getItem() instanceof IModeChangingItem))
            return modes;

        IModuleManager moduleManager = ((IModularItem)stack.getItem()).getModuleManager();
        for (Object module : moduleManager.getModulesOfType(IRightClickModule.class)) {
            if (moduleManager.isValidForItem(stack, (IPowerModule) module))
                if (moduleManager.itemHasModule(stack, ((IPowerModule) module).getDataName()))
                    modes.add(((IPowerModule) module).getDataName());
        }
        return modes;
    }

    default String getActiveMode(@Nonnull ItemStack stack) {
        String modeFromNBT = NBTUtils.getMuseItemTag(stack).getString(MPALIbConstants.TAG_MODE);
        if (modeFromNBT.isEmpty()) {
            List<String> validModes = getValidModes(stack);
            return (validModes != null && (validModes.size() > 0) ? validModes.get(0) : "");
        } else
            return modeFromNBT;
    }

    default void setActiveMode(@Nonnull ItemStack stack, String newMode) {
        String activeMode = getActiveMode(stack);
        IPowerModule oldModule = getModuleManager().getModule(activeMode);
        if (oldModule instanceof IEnchantmentModule)
            ((IEnchantmentModule) oldModule).removeEnchantment(stack);
        NBTUtils.getMuseItemTag(stack).setString(MPALIbConstants.TAG_MODE, newMode);
        IPowerModule module = getModuleManager().getModule(newMode);
        if (module instanceof IEnchantmentModule)
            ((IEnchantmentModule) module).addEnchantment(stack);
    }

    default void cycleMode(ItemStack stack, EntityPlayer player, int dMode) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(this.getActiveMode(stack)) + dMode, modes.size());
            String newmode = (String) modes.get(newindex);
            this.setActiveMode(stack, newmode);
            MPALibPackets.sendToServer(new LegacyModeChangeRequestPacket(newmode, player.inventory.currentItem));
        }
    }

    /* nextMode and prevMode are used for getting the icons to display in the mode selection */
    default String nextMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) + 1, modes.size());
            return (String) modes.get(newindex);
        } else return "";
    }

    default String prevMode(ItemStack stack, EntityPlayer player) {
        List<String> modes = this.getValidModes(stack);
        if (modes.size() > 0) {
            int newindex = clampMode(modes.indexOf(getActiveMode(stack)) - 1, modes.size());
            return (String) modes.get(newindex);
        } else return "";
    }

    default int clampMode(int selection, int modesSize) {
        return (selection > 0) ? (selection % modesSize) : ((selection + modesSize * -selection) % modesSize);
    }
}