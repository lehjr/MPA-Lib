package com.github.lehjr.mpalib.nbt.propertymodifier;

import net.minecraft.nbt.CompoundNBT;

public interface IPropertyModifierDouble extends IPropertyModifier<Double> {
    @Override
    Double applyModifier(CompoundNBT moduleTag, Double value);
}