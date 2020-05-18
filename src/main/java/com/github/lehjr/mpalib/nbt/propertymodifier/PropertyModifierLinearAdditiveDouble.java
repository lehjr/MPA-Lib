package com.github.lehjr.mpalib.nbt.propertymodifier;

import com.github.lehjr.mpalib.nbt.NBTUtils;
import net.minecraft.nbt.CompoundNBT;

public class PropertyModifierLinearAdditiveDouble implements IPropertyModifierDouble {
    public final String tradeoffName;
    public double multiplier;

    public PropertyModifierLinearAdditiveDouble(String tradeoffName, double multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public Double applyModifier(CompoundNBT moduleTag, Double value) {
        return value + multiplier * NBTUtils.getDoubleOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}