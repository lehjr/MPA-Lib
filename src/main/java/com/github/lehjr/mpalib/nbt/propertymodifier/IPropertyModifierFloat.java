package com.github.lehjr.mpalib.nbt.propertymodifier;

import net.minecraft.nbt.CompoundNBT;

public interface IPropertyModifierFloat extends IPropertyModifier<Float> {
    @Override
    Float applyModifier(CompoundNBT moduleTag, Float value);
}