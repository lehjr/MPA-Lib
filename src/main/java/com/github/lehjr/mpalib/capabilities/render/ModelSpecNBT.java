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

package com.github.lehjr.mpalib.capabilities.render;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.github.lehjr.mpalib.client.render.modelspec.EnumSpecType;
import com.github.lehjr.mpalib.nbt.NBTUtils;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ModelSpecNBT implements IModelSpecNBT, INBTSerializable<NBTTagCompound> {
    ItemStack itemStack;
    static final String TAG_RENDER = "render";

    public ModelSpecNBT(@Nonnull ItemStack itemStackIn){
        this.itemStack = itemStackIn;
    }

    @Nonnull
    @Override
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Override
    public NBTTagCompound setMuseRenderTag(NBTTagCompound renderDataIn, String tagName) {
        NBTTagCompound itemTag = NBTUtils.getMuseItemTag(itemStack);
        if (tagName != null) {
            if (Objects.equals(tagName, MPALIbConstants.TAG_RENDER)) {
                itemTag.removeTag(MPALIbConstants.TAG_RENDER);
                if (!renderDataIn.isEmpty())
                    itemTag.setTag(MPALIbConstants.TAG_RENDER, renderDataIn);
            } else {
                NBTTagCompound renderTag;
                if (!itemTag.hasKey(MPALIbConstants.TAG_RENDER)) {
                    renderTag = new NBTTagCompound();
                    itemTag.setTag(MPALIbConstants.TAG_RENDER, renderTag);
                } else {
                    renderTag = itemTag.getCompoundTag(MPALIbConstants.TAG_RENDER);
                }
                if (renderDataIn.isEmpty()) {
                    MPALibLogger.logger.debug("Removing tag " + tagName);
                    renderTag.removeTag(tagName);
                } else {
                    MPALibLogger.logger.debug("Adding tag " + tagName + " : " + renderDataIn);
                    renderTag.setTag(tagName, renderDataIn);
                }
            }
        }
        return getMuseRenderTag();
    }

    @Override
    public EnumSpecType getSpecType() {
        if (itemStack.getItem().getEquipmentSlot(itemStack) == null) {
            return EnumSpecType.HANDHELD;
        }
        return EnumSpecType.NONE;
    }

    @Override
    @Nullable
    public NBTTagCompound getMuseRenderTag() {
        NBTTagCompound itemTag = NBTUtils.getMuseItemTag(itemStack);
        return itemTag.getCompoundTag(TAG_RENDER);
    }

    @Override
    public NBTTagCompound getDefaultRenderTag() {
        return new NBTTagCompound();
    }

    /**
     * When dealing with possibly multiple specs and color lists, new list needs to be created, since there is only one list per item.
     */
    @Override
    public List<Integer> addNewColourstoList(List<Integer> colours, List<Integer> coloursToAdd) {
        for (Integer i : coloursToAdd) {
            if (!colours.contains(i))
                colours.add(i);
        }
        return colours;
    }

    @Override
    public int[] getColorArray() {
        return  getMuseRenderTag().getIntArray(MPALIbConstants.TAG_COLOURS);
    }


    /**
     * new array means setting a new array index for the same getValue
     */
    @Override
    public int getNewColourIndex(List<Integer> colours, List<Integer> oldColours, Integer index) {
        return colours.indexOf(oldColours.get(index != null ? index : 0));
    }

    @Override
    public NBTTagCompound setColorArray(int[] colors) {
        getMuseRenderTag().setIntArray(MPALIbConstants.TAG_COLOURS, colors);
        return getMuseRenderTag();
    }

    // NBTBaseSerializable<NBTTagCompound> ----------------------------------------------------------------------------------
    @Override
    public NBTTagCompound serializeNBT() {
        return getMuseRenderTag();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        setMuseRenderTag(nbt, MPALIbConstants.TAG_RENDER);
    }
}