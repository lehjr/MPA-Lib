package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.basemod.ModObjects;
import net.minecraft.item.Item;

public class ItemComponent extends Item {
    public ItemComponent() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModObjects.creativeTab)
                .defaultMaxDamage(-1)
                .setNoRepair());
    }
}