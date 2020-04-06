package com.github.lehjr.mpalib.nbt.propertymodifier;

import net.minecraft.nbt.CompoundNBT;

public class PropertyModifierFlatAdditiveFloat implements IPropertyModifierFloat {
    public float valueAdded;

    public PropertyModifierFlatAdditiveFloat(float valueAdded) {
        this.valueAdded = valueAdded;
    }

    /**
     * @param moduleTag unused
     * @param value
     * @return getValue + this.valueAdded
     */
    @Override
    public Float applyModifier(CompoundNBT moduleTag, Float value) {
        return value + this.valueAdded;
    }
}