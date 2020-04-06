package com.github.lehjr.mpalib.nbt.propertymodifier;

import com.github.lehjr.mpalib.nbt.NBTUtils;
import net.minecraft.nbt.CompoundNBT;

public class PropertyModifierLinearAdditiveFloat implements IPropertyModifierFloat {
    public final String tradeoffName;
    public float multiplier;

    public PropertyModifierLinearAdditiveFloat(String tradeoffName, Float multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public Float applyModifier(CompoundNBT moduleTag, Float value) {
        return value + multiplier * NBTUtils.getFloatOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}