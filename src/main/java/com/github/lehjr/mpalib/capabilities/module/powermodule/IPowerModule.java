/*
 * Copyright (c) 2019 leon
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

import com.github.lehjr.mpalib.nbt.propertymodifier.IPropertyModifierDouble;
import com.github.lehjr.mpalib.nbt.propertymodifier.IPropertyModifierInteger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
    /**
     * Returns the enum corresponding to the EntityEquipment slot that the parent itemStack (Head, Chest... ALL.. )
     *
     * @return
     */
    ItemStack getModuleStack();

    EnumModuleTarget getTarget();

    EnumModuleCategory getCategory();

    @OnlyIn(Dist.CLIENT)
    String getUnit(String propertyName);

    void addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier);

    void addPropertyModifier(String propertyName, IPropertyModifierDouble modifier);

    void addTradeoffPropertyDouble(String tradeoffName, String propertyName, double multiplier, String unit);

    void addBasePropertyDouble(String propertyName, double baseVal);

    void addBasePropertyDouble(String propertyName, double baseVal, String unit);

    double applyPropertyModifiers(String propertyName);

    double applyPropertyModifiers(String propertyName, CompoundNBT moduleTag);

    Map<String, List<IPropertyModifierDouble>> getPropertyModifiers();

    void addIntTradeoffProperty(String tradeoffName, String propertyName, int multiplier, String unit, int roundTo, int offset);

    void addBasePropertyInteger(String propertyName, int baseVal);

    void addBasePropertyInteger(String propertyName, int baseVal, String unit);

    void addPropertyModifierInteger(String propertyName, IPropertyModifierInteger modifier);

    // For use with integer base values only. Does not play well with doubles.
    int applyPropertyModifierBaseInt(String propertyName);

    Map<String, List<IPropertyModifierInteger>> getPropertyModifierBaseInt();

    boolean isAllowed();
}