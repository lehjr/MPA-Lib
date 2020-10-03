package com.github.lehjr.mpalib.basemod;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class MPALibCreativeTab extends ItemGroup {
    public MPALibCreativeTab() {
        super(MPALIbConstants.MOD_ID);
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModObjects.ARMOR_STAND_ITEM.get());
    }
}
