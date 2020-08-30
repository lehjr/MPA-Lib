package com.github.lehjr.mpalib.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class MPALibContainerProvier implements INamedContainerProvider {
    int typeIndex;
    public MPALibContainerProvier(int typeIndex) {
        this.typeIndex = typeIndex;
    }

    @Override
    public ITextComponent getDisplayName() {
        switch(typeIndex) {
            case 0:
                return new TranslationTextComponent("gui.mpalib.tab.tinker");
            default:
                return new TranslationTextComponent("container.crafting");
        }
    }

    @Nullable
    @Override
    public Container createMenu(int windowId, PlayerInventory playerInventory, PlayerEntity player) {
        switch(typeIndex) {
            case 0:
                return new ArmorStandModdingContainer(windowId, playerInventory);
            default:
                return new CraftingContainer(windowId, playerInventory);
        }
    }
}
