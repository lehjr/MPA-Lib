package com.github.lehjr.mpalib.util.nbt.propertymodifier;

import com.github.lehjr.mpalib.util.nbt.NBTUtils;
import net.minecraft.nbt.CompoundNBT;

public class PropertyModifierLinearAdditive implements IPropertyModifier {
    public final String tradeoffName;
    public double multiplier;

    public PropertyModifierLinearAdditive(String tradeoffName, double multiplier) {
        this.multiplier = multiplier;
        this.tradeoffName = tradeoffName;
    }

    @Override
    public double applyModifier(CompoundNBT moduleTag, double value) {
        return value + multiplier * NBTUtils.getDoubleOrZero(moduleTag, tradeoffName);
    }

    public String getTradeoffName() {
        return tradeoffName;
    }
}