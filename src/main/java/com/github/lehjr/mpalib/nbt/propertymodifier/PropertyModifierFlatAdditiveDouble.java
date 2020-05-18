package com.github.lehjr.mpalib.nbt.propertymodifier;

import net.minecraft.nbt.CompoundNBT;

public class PropertyModifierFlatAdditiveDouble implements IPropertyModifierDouble {
    public double valueAdded;

    public PropertyModifierFlatAdditiveDouble(double valueAdded) {
        this.valueAdded = valueAdded;
    }

    /**
     * @param moduleTag unused
     * @param value
     * @return getValue + this.valueAdded
     */
    @Override
    public Double applyModifier(CompoundNBT moduleTag, Double value) {
        return value + this.valueAdded;
    }
}