package com.github.lehjr.mpalib.module.legacy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public interface IPlayerTickModule extends IPowerModule {
    void onPlayerTickActive(EntityPlayer player, @Nonnull ItemStack item);

    void onPlayerTickInactive(EntityPlayer player, @Nonnull ItemStack item);
}
