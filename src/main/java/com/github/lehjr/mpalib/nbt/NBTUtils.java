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
import net.minecraft.nbt.NBTTagCompound;
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
     * @return an NBTTagCompound, may be newly created. If stack is empty, returns null.
     */
    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return new NBTTagCompound();

        NBTTagCompound stackTag = stack.getTagCompound() != null ? stack.getTagCompound() : new NBTTagCompound();



        NBTTagCompound properties = (stackTag.hasKey(TAG_ITEM_PREFIX)) ? stackTag.getCompoundTag(TAG_ITEM_PREFIX) : new NBTTagCompound();
        stackTag.setTag(TAG_ITEM_PREFIX, properties);
        stack.setTagCompound(stackTag);
        return properties;
    }

    /**
     * Gets or creates stack.getTag.getTag(TAG_MODULE_PREFIX)
     *
     * @param module
     * @return an NBTTagCompound, may be newly created. If stack is empty, returns null.
     */
    public static NBTTagCompound getMuseModuleTag(@Nonnull ItemStack module) {
        if (module.isEmpty())
            return new NBTTagCompound();

        NBTTagCompound stackTag = module.getTagCompound() != null ? module.getTagCompound() : new NBTTagCompound();
        NBTTagCompound properties = (stackTag.hasKey(TAG_MODULE_PREFIX)) ? stackTag.getCompoundTag(TAG_MODULE_PREFIX) : new NBTTagCompound();
        stackTag.setTag(TAG_MODULE_PREFIX, properties);
        module.setTagCompound(stackTag);
        return properties;
    }

    // Doubles --------------------------------------------------------------------------------------------------------
    /**
     * Bouncer for succinctness. Checks the itemStack's modular properties and
     * returns the getValue if it exists, otherwise 0.
     */
    public static double getModuleDoubleOrZero(@Nonnull ItemStack stack, String string) {
        return getDoubleOrZero(getMuseModuleTag(stack), string);
    }

    public static double getModularItemDoubleOrZero(@Nonnull ItemStack stack, String string) {
        return getDoubleOrZero(getMuseItemTag(stack), string);
    }

    public static double getDoubleOrZero(NBTTagCompound nbt, String tagName) {
        return (nbt.hasKey(tagName, Constants.NBT.TAG_DOUBLE) ? nbt.getDouble(tagName) : 0);
    }

    /**
     * Sets the given itemstack's modular property, or removes it if the getValue
     * would be zero.
     */
    public static void setModularItemDoubleOrRemove(@Nonnull ItemStack stack, String string, double value) {
        setDoubleOrRemove(NBTUtils.getMuseItemTag(stack), string, value);
    }

    public static void setModuleDoubleOrRemove(@Nonnull ItemStack stack, String string, double value) {
        setDoubleOrRemove(NBTUtils.getMuseModuleTag(stack), string, value);
    }

    /**
     * Sets the getValue of the given nbt tag, or removes it if the getValue would be
     * zero.
     */
    public static void setDoubleOrRemove(NBTTagCompound itemProperties, String string, double value) {
        if (itemProperties != null) {
            if (value == 0) {
                itemProperties.removeTag(string);
            } else {
                itemProperties.setDouble(string, value);
            }
        }
    }

    // Integers -------------------------------------------------------------------------------------------------------
    /**
     * Bouncer for succinctness. Checks the itemStack's modular properties and
     * returns the getValue if it exists, otherwise 0.
     */
    public static int getModuleIntOrZero(@Nonnull ItemStack module, String string) {
        return getIntOrZero(getMuseModuleTag(module), string);
    }

    public static int getModularItemIntOrZero(@Nonnull ItemStack module, String string) {
        return getIntOrZero(getMuseItemTag(module), string);
    }

    static int getIntOrZero(NBTTagCompound nbt, String tagName) {
        return (nbt.hasKey(tagName, Constants.NBT.TAG_INT) ? nbt.getInteger(tagName) : 0);
    }

    public static void setModuleIntOrRemove(@Nonnull ItemStack stack, String tagName, int value) {
        setIntOrRemove(getMuseModuleTag(stack), tagName, value);
    }

    public static void setModularItemIntOrRemove(@Nonnull ItemStack stack, String tagName, int value) {
        setIntOrRemove(getMuseItemTag(stack), tagName, value);
    }

    public static void setIntOrRemove(@Nonnull NBTTagCompound nbt, String tagName, int value) {
        if (value == 0)
            nbt.removeTag(tagName);
        else
            nbt.setInteger(tagName, value);
    }

    // Boolean --------------------------------------------------------------------------------------------------------
    public static boolean getModuleBooleanOrSetDefault(@Nonnull ItemStack module, String tagName, boolean defBool) {
        NBTTagCompound moduleTag = getMuseModuleTag(module);
        if (moduleTag.hasKey(tagName, Constants.NBT.TAG_BYTE)) {
            return getBooleanOrFalse(moduleTag, tagName);
        } else {
            moduleTag.setBoolean(tagName, defBool);
            return defBool;
        }
    }

    public static boolean getModuleBooleanOrFalse(@Nonnull ItemStack module, String string) {
        return getBooleanOrFalse(getMuseModuleTag(module), string);
    }

    public static boolean getItemBooleanOrFalse(@Nonnull ItemStack module, String string) {
        return getBooleanOrFalse(getMuseItemTag(module), string);
    }

    static boolean getBooleanOrFalse(NBTTagCompound nbt, String tagName) {
        return (nbt.hasKey(tagName, Constants.NBT.TAG_BYTE) ? nbt.getBoolean(tagName) : false);
    }

    public static void setModuleBoolean(@Nonnull ItemStack module, String string, boolean value) {
        getMuseModuleTag(module).setBoolean(string, value);
    }

    public static void setModularItemBoolean(@Nonnull ItemStack module, String string, boolean value) {
        getMuseItemTag(module).setBoolean(string, value);
    }

    // Store commonly recalculated values in a compound tag.
    @Nullable
    public static NBTTagCompound getMuseValuesTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty())
            return null;

        NBTTagCompound itemTag = getMuseItemTag(stack);
        NBTTagCompound valuesTag;
        if (itemTag.hasKey(TAG_VALUES)) {
            valuesTag = itemTag.getCompoundTag(TAG_VALUES);
        } else {
            valuesTag = new NBTTagCompound();
            itemTag.setTag(TAG_VALUES, valuesTag);
        }
        return valuesTag;
    }

    public static void removeMuseValuesTag(@Nonnull ItemStack stack) {
        NBTTagCompound itemTag = getMuseItemTag(stack);
        itemTag.removeTag(TAG_VALUES);
    }

    /**
     * Replaced in later versions of Minecraft by ItemStack.getOrCreateTag()
     */
    public static NBTTagCompound getNBTTag(@Nonnull ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.hasTagCompound()) {
            return itemStack.getTagCompound();
        } else {
            NBTTagCompound tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
            return tag;
        }
    }


    public static String getStringOrNull(@Nonnull ItemStack stack, String key) {
        return getStringOrNull(getMuseItemTag(stack), key);
    }

    public static String getStringOrNull(@Nonnull NBTTagCompound itemProperties, String key) {
        String value = null;
        if (itemProperties != null) {
            if (itemProperties.hasKey(key)) {
                value = itemProperties.getString(key);
            }
        }
        return value;
    }

    public static void setStringOrNull(NBTTagCompound itemProperties, String key, String value) {
        if (itemProperties != null) {
            if (value.isEmpty()) {
                itemProperties.removeTag(key);
            } else {
                itemProperties.setString(key, value);
            }
        }
    }

    public static void setStringOrNull(@Nonnull ItemStack stack, String key, String value) {
        setStringOrNull(getMuseItemTag(stack), key, value);
    }
}