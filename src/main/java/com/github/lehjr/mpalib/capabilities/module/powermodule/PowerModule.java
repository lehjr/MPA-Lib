/*
 * Copyright (c) 2019 MachineMuse, Lehjr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.lehjr.mpalib.capabilities.module.powermodule;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.capabilities.IConfig;
import com.github.lehjr.mpalib.nbt.MuseNBTUtils;
import com.github.lehjr.mpalib.nbt.propertymodifier.*;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PowerModule implements IPowerModule {
    protected static Map<String, String> units;
    protected ItemStack module;
    protected final EnumModuleCategory category;
    protected final EnumModuleTarget target;
    protected Map<String, List<IPropertyModifierDouble>> propertyModifiers;
    protected Map<String, List<IPropertyModifierInteger>> propertyBaseIntegers;
    IConfig config;

    public PowerModule(@Nonnull ItemStack module, EnumModuleCategory category, EnumModuleTarget target, IConfig config) {
        propertyModifiers = new HashMap<>();
        propertyBaseIntegers = new HashMap<>();
        units = new HashMap<>();
        this.module = module;
        this.category = category;
        this.target = target;
        this.config = config;
    }

    @Override
    public ItemStack getModuleStack() {
        return module;
    }

    @Override
    public EnumModuleTarget getTarget() {
        return target;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return category;
    }


    // TODO: move to somewhere else??
    @OnlyIn(Dist.CLIENT)
    @Override
    public String getUnit(String propertyName) {
        String unit = units.get(propertyName);
        if (unit != null && unit.startsWith(MPALIbConstants.MODULE_TRADEOFF_PREFIX))
            unit = I18n.format(unit);

        return unit == null ? "" : unit;
    }

    /** Double ------------------------------------------------------------------------------------ */
    /**
     * Adds a base key and multiplierValue to the map based on the config setting.
     */
    @Override
    public void addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier) {
        double propFromConfig = config.getTradeoffPropertyDoubleOrDefault(category, module, tradeoffName, propertyName, multiplier);
        addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveDouble(tradeoffName, propFromConfig));
    }

    @Override
    public void addPropertyModifier(String propertyName, IPropertyModifierDouble modifier) {
        List<IPropertyModifierDouble> modifiers = propertyModifiers.get(propertyName);
        if (modifiers == null) {
            modifiers = new LinkedList();
        }
        modifiers.add(modifier);
        propertyModifiers.put(propertyName, modifiers);
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    @Override
    public void addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier, String unit) {
        units.put(propertyName, unit);
        addTradeoffPropertyDouble(tradeoffName, propertyName, multiplier);
    }

    public void addSimpleTradeoffDouble(IPowerModule module,
                                        String tradeoffName,
                                        String firstPropertyName,
                                        String firstUnits,
                                        double firstPropertyBase,
                                        double firstPropertyMultiplier,
                                        String secondPropertyName,
                                        String secondUnits,
                                        double secondPropertyBase,
                                        double secondPropertyMultiplier) {
        this.addBasePropertyDouble(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffPropertyDouble(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBasePropertyDouble(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffPropertyDouble(tradeoffName, secondPropertyName, secondPropertyMultiplier);
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     */
    @Override
    public void addBasePropertyDouble(String propertyName, double baseVal) {
        double propFromConfig = config.getBasePropertyDoubleOrDefault(category, module, propertyName, baseVal);
        addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveDouble(propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    @Override
    public void addBasePropertyDouble(String propertyName, double baseVal, String unit) {
        units.put(propertyName, unit);
        addBasePropertyDouble(propertyName, baseVal);
    }

    @Override
    public double applyPropertyModifiers(String propertyName) {
        return applyPropertyModifiers(propertyName, MuseNBTUtils.getMuseModuleTag(module));
    }

    @Override
    public double applyPropertyModifiers(String propertyName, CompoundNBT moduleTag) {
        double propertyValue = 0;
        if (propertyModifiers.containsKey(propertyName)) {
            Iterable<IPropertyModifierDouble> propertyModifiersIterable = propertyModifiers.get(propertyName);
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                propertyValue = ((IPropertyModifierDouble) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    /*
        TODO: method of getting min/max values with names and labels. a 1.0D value on the NBT should yield a max val,
         while 0 will yield a min val
    */

    @Override
    public Map<String, List<IPropertyModifierDouble>> getPropertyModifiers() {
        return propertyModifiers;
    }

    /**
     * Integer -----------------------------------------------------------------------------------
     */

    // This is the only one of these that will give an integer using the double system
    public void addIntTradeoffProperty(String tradeoffName, String propertyName, int multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        int propFromConfig = config.getTradeoffPropertyIntegerOrDefault(category, module, tradeoffName, propertyName, multiplier);
        addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, propFromConfig, roundTo, offset));
    }

    @Override
    public void addBasePropertyInteger(String propertyName, int baseVal) {
        int propFromConfig = config.getBasePropertIntegerOrDefault(category, module, propertyName, baseVal);
        addPropertyModifierInteger(propertyName, new PropertyModifierFlatAdditiveInteger(propFromConfig));
    }

    @Override
    public void addBasePropertyInteger(String propertyName, int baseVal, String unit) {
        units.put(propertyName, unit);
        addBasePropertyInteger(propertyName, baseVal);
    }

    @Override
    public void addPropertyModifierInteger(String propertyName, IPropertyModifierInteger modifier) {
        List<IPropertyModifierInteger> modifiers = propertyBaseIntegers.get(propertyName);
        if (modifiers == null) {
            modifiers = new LinkedList();
        }
        modifiers.add(modifier);
        propertyBaseIntegers.put(propertyName, modifiers);
    }

    @Override
    public int applyPropertyModifierBaseInt(String propertyName) {
        int propertyValue = 0;
        Iterable<IPropertyModifierInteger> propertyModifiersIterable = propertyBaseIntegers.get(propertyName);
        CompoundNBT moduleTag = MuseNBTUtils.getMuseModuleTag(getModuleStack());
        for (IPropertyModifier modifier : propertyModifiersIterable) {
            propertyValue = ((IPropertyModifierInteger) modifier).applyModifier(moduleTag, propertyValue);
        }
        return propertyValue;
    }

    @Override
    public Map<String, List<IPropertyModifierInteger>> getPropertyModifierBaseInt() {
        return propertyBaseIntegers;
    }

    @Override
    public boolean isAllowed() {
        return config.isModuleAllowed(category, module);
    }
}