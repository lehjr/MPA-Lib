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
import com.github.lehjr.mpalib.nbt.NBTUtils;
import com.github.lehjr.mpalib.nbt.propertymodifier.*;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class PowerModule implements IPowerModule {
    protected static Map<String, String> units;
    protected ItemStack module;
    protected final EnumModuleCategory category;
    protected final EnumModuleTarget target;
    protected Map<String, List<IPropertyModifierFloat>> propertyModifiers;
    protected Map<String, List<IPropertyModifierInteger>> propertyBaseIntegers;
    Callable<IConfig> moduleConfigGetter;

    public PowerModule(@Nonnull ItemStack module, EnumModuleCategory category, EnumModuleTarget target,
                       Callable<IConfig> moduleConfigGetterIn) {
        propertyModifiers = new HashMap<>();
        propertyBaseIntegers = new HashMap<>();
        units = new HashMap<>();
        this.module = module;
        this.category = category;
        this.target = target;
        this.moduleConfigGetter = moduleConfigGetterIn;
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

    Optional<IConfig> getConfig() {
        try {
            return Optional.ofNullable(moduleConfigGetter.call());
        } catch (Exception e) {
            // not initialized yet
            // TODO: debug message?
//            e.printStackTrace();
        }
        return null;
    }


    /** Float ------------------------------------------------------------------------------------- */
    /**
     * Adds a base key and multiplierValue to the map based on the config setting.
     */
    @Override
    public void addTradeoffPropertyFloat(String tradeoffName, String propertyName, float multiplier) {
        float propFromConfig = getConfig().map(config->
                config.getTradeoffPropertyFloatOrDefault(category, module, tradeoffName, propertyName, multiplier)).orElse(multiplier);
        addPropertyModifier(propertyName, new PropertyModifierLinearAdditiveFloat(tradeoffName, propFromConfig));
    }

    @Override
    public void addPropertyModifier(String propertyName, IPropertyModifierFloat modifier) {
        List<IPropertyModifierFloat> modifiers = propertyModifiers.get(propertyName);
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
    public void addTradeoffPropertyFloat(String tradeoffName, String propertyName, float multiplier, String unit) {
        units.put(propertyName, unit);
        addTradeoffPropertyFloat(tradeoffName, propertyName, multiplier);
    }

    public void addSimpleTradeoffFloat(IPowerModule module,
                                        String tradeoffName,
                                        String firstPropertyName,
                                        String firstUnits,
                                        float firstPropertyBase,
                                        float firstPropertyMultiplier,
                                        String secondPropertyName,
                                        String secondUnits,
                                        float secondPropertyBase,
                                        float secondPropertyMultiplier) {
        this.addBasePropertyFloat(firstPropertyName, firstPropertyBase, firstUnits);
        this.addTradeoffPropertyFloat(tradeoffName, firstPropertyName, firstPropertyMultiplier);
        this.addBasePropertyFloat(secondPropertyName, secondPropertyBase, secondUnits);
        this.addTradeoffPropertyFloat(tradeoffName, secondPropertyName, secondPropertyMultiplier);
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     */
    @Override
    public void addBasePropertyFloat(String propertyName, float baseVal) {
        float propFromConfig =
                getConfig().map(config->
                        config.getBasePropertyFloatOrDefault(category, module, propertyName, baseVal)).orElse(baseVal);
        addPropertyModifier(propertyName, new PropertyModifierFlatAdditiveFloat(propFromConfig));
    }

    /**
     * Adds a base key and getValue to the map based on the config setting.
     * Also adds a [ propertyName, unitOfMeasureLabel ] k-v pair to a map used for displyaing a label
     */
    @Override
    public void addBasePropertyFloat(String propertyName, float baseVal, String unit) {
        units.put(propertyName, unit);
        addBasePropertyFloat(propertyName, baseVal);
    }

    @Override
    public float applyPropertyModifiers(String propertyName) {
        return applyPropertyModifiers(propertyName, NBTUtils.getModuleTag(module));
    }

    @Override
    public float applyPropertyModifiers(String propertyName, CompoundNBT moduleTag) {
        float propertyValue = 0;
        if (propertyModifiers.containsKey(propertyName)) {
            Iterable<IPropertyModifierFloat> propertyModifiersIterable = propertyModifiers.get(propertyName);
            for (IPropertyModifier modifier : propertyModifiersIterable) {
                propertyValue = ((IPropertyModifierFloat) modifier).applyModifier(moduleTag, propertyValue);
            }
        }
        return propertyValue;
    }

    /*
        TODO: method of getting min/max values with names and labels. a 1.0D value on the NBT should yield a max val,
         while 0 will yield a min val
    */

    @Override
    public Map<String, List<IPropertyModifierFloat>> getPropertyModifiers() {
        return propertyModifiers;
    }

    /**
     * Integer -----------------------------------------------------------------------------------
     */

    // This is the only one of these that will give an integer using the double system
    public void addIntTradeoffProperty(String tradeoffName, String propertyName, int multiplier, String unit, int roundTo, int offset) {
        units.put(propertyName, unit);
        int propFromConfig = getConfig().map(config->
                config.getTradeoffPropertyIntegerOrDefault(category, module, tradeoffName, propertyName, multiplier)).orElse(multiplier);
        addPropertyModifier(propertyName, new PropertyModifierIntLinearAdditive(tradeoffName, propFromConfig, roundTo, offset));
    }

    @Override
    public void addBasePropertyInteger(String propertyName, int baseVal) {
        int propFromConfig = getConfig().map(config->
                config.getBasePropertIntegerOrDefault(category, module, propertyName, baseVal)).orElse(baseVal);
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
        CompoundNBT moduleTag = NBTUtils.getModuleTag(getModuleStack());
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
        return getConfig().map(config-> config.isModuleAllowed(category, module)).orElse(true);
    }
}