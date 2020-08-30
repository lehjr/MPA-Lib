package com.github.lehjr.mpalib.container;

import net.minecraft.inventory.container.Container;
import net.minecraftforge.items.SlotItemHandler;

import java.util.List;
import java.util.Map;

public interface IModularItemToSlotMapProvider<T extends Container> {
    Map<Integer, List<SlotItemHandler>> getModularItemToSlotMap();

    T getContainer();
}
