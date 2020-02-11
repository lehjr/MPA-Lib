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

package com.github.lehjr.mpalib.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NBTUtils {
    public static final String TAG_ITEM_PREFIX = "MMModItem";// Machine Muse Mod
    public static final String TAG_MODULE_PREFIX = "MMModModule";// Machine Muse Mod
    public static final String TAG_VALUES = "commonValues"; // commonly used values that would normally be recalculated several times a minute.

    /**
     * Gets or creates stack.getTag.getTag(TAG_ITEM_PREFIX)
     *
     * @param stack
     * @return an CompoundNBT, may be newly created. If stack is empty, returns null.
     */
    public static CompoundNBT getMuseItemTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return new CompoundNBT();

        CompoundNBT stackTag = stack.getOrCreateTag();
        CompoundNBT properties = (stackTag.contains(TAG_ITEM_PREFIX)) ? stackTag.getCompound(TAG_ITEM_PREFIX) : new CompoundNBT();
        stackTag.put(TAG_ITEM_PREFIX, properties);
        stack.setTag(stackTag);
        return properties;
    }

    /**
     * Gets or creates stack.getTag.getTag(TAG_MODULE_PREFIX)
     *
     * @param module
     * @return an CompoundNBT, may be newly created. If stack is empty, returns null.
     */
    public static CompoundNBT getModuleTag(@Nonnull ItemStack module) {
        if (module.isEmpty())
            return new CompoundNBT();

        CompoundNBT stackTag = module.getOrCreateTag();
        CompoundNBT properties = (stackTag.contains(TAG_MODULE_PREFIX)) ? stackTag.getCompound(TAG_MODULE_PREFIX) : new CompoundNBT();
        stackTag.put(TAG_MODULE_PREFIX, properties);
        module.setTag(stackTag);
        return properties;
    }

    // Doubles --------------------------------------------------------------------------------------------------------
    /**
     * Bouncer for succinctness. Checks the itemStack's modular properties and
     * returns the getValue if it exists, otherwise 0.
     */
    public static double getModuleDoubleOrZero(@Nonnull ItemStack stack, String string) {
        return getDoubleOrZero(getModuleTag(stack), string);
    }

    public static double getModularItemDoubleOrZero(@Nonnull ItemStack stack, String string) {
        return getDoubleOrZero(getMuseItemTag(stack), string);
    }

    public static double getDoubleOrZero(CompoundNBT nbt, String tagName) {
        return (nbt.contains(tagName, Constants.NBT.TAG_DOUBLE) ? nbt.getDouble(tagName) : 0);
    }

    /**
     * Sets the given itemstack's modular property, or removes it if the getValue
     * would be zero.
     */
    public static void setModularItemDoubleOrRemove(@Nonnull ItemStack stack, String string, double value) {
        setDoubleOrRemove(NBTUtils.getMuseItemTag(stack), string, value);
    }

    public static void setModuleDoubleOrRemove(@Nonnull ItemStack stack, String string, double value) {
        setDoubleOrRemove(NBTUtils.getModuleTag(stack), string, value);
    }

    /**
     * Sets the getValue of the given nbt tag, or removes it if the getValue would be
     * zero.
     */
    public static void setDoubleOrRemove(CompoundNBT itemProperties, String string, double value) {
        if (itemProperties != null) {
            if (value == 0) {
                itemProperties.remove(string);
            } else {
                itemProperties.putDouble(string, value);
            }
        }
    }

    // Integers -------------------------------------------------------------------------------------------------------
    /**
     * Bouncer for succinctness. Checks the itemStack's modular properties and
     * returns the getValue if it exists, otherwise 0.
     */
    public static int getModuleIntOrZero(@Nonnull ItemStack module, String string) {
        return getIntOrZero(getModuleTag(module), string);
    }

    public static int getModularItemIntOrZero(@Nonnull ItemStack module, String string) {
        return getIntOrZero(getMuseItemTag(module), string);
    }

    static int getIntOrZero(CompoundNBT nbt, String tagName) {
        return (nbt.contains(tagName, Constants.NBT.TAG_INT) ? nbt.getInt(tagName) : 0);
    }

    public static void setModuleIntOrRemove(@Nonnull ItemStack stack, String tagName, int value) {
        setIntOrRemove(getModuleTag(stack), tagName, value);
    }

    public static void setModularItemIntOrRemove(@Nonnull ItemStack stack, String tagName, int value) {
        setIntOrRemove(getMuseItemTag(stack), tagName, value);
    }

    public static void setIntOrRemove(@Nonnull CompoundNBT nbt, String tagName, int value) {
        if (value == 0)
            nbt.remove(tagName);
        else
            nbt.putInt(tagName, value);
    }

    // Boolean --------------------------------------------------------------------------------------------------------
    public static boolean getModuleBooleanOrSetDefault(@Nonnull ItemStack module, String tagName, boolean defBool) {
        CompoundNBT moduleTag = getModuleTag(module);
        if (moduleTag.contains(tagName, Constants.NBT.TAG_BYTE)) {
            return getBooleanOrFalse(moduleTag, tagName);
        } else {
            moduleTag.putBoolean(tagName, defBool);
            return defBool;
        }
    }

    public static boolean getModuleBooleanOrFalse(@Nonnull ItemStack module, String string) {
        return getBooleanOrFalse(getModuleTag(module), string);
    }

    public static boolean getItemBooleanOrFalse(@Nonnull ItemStack module, String string) {
        return getBooleanOrFalse(getMuseItemTag(module), string);
    }

    static boolean getBooleanOrFalse(CompoundNBT nbt, String tagName) {
        return (nbt.contains(tagName, Constants.NBT.TAG_BYTE) ? nbt.getBoolean(tagName) : false);
    }

    public static void setModuleBoolean(@Nonnull ItemStack module, String string, boolean value) {
        getModuleTag(module).putBoolean(string, value);
    }

    public static void setModularItemBoolean(@Nonnull ItemStack module, String string, boolean value) {
        getMuseItemTag(module).putBoolean(string, value);
    }

    // Store commonly recalculated values in a compound tag.
    @Nullable
    public static CompoundNBT getMuseValuesTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return null;

        CompoundNBT itemTag = getMuseItemTag(stack);
        CompoundNBT valuesTag;
        if (itemTag.contains(TAG_VALUES)) {
            valuesTag = itemTag.getCompound(TAG_VALUES);
        } else {
            valuesTag = new CompoundNBT();
            itemTag.put(TAG_VALUES, valuesTag);
        }
        return valuesTag;
    }

    public static void removeMuseValuesTag(@Nonnull ItemStack stack) {
        CompoundNBT itemTag = getMuseItemTag(stack);
        itemTag.remove(TAG_VALUES);
    }
}