package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.basemod.ModObjects;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemComponent extends Item {
    public ItemComponent() {
        super(new Item.Properties()
                .maxStackSize(64)
                .group(ModObjects.creativeTab)
                .defaultMaxDamage(-1)
                .setNoRepair());
    }
}