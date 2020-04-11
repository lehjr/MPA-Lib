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

package com.github.lehjr.mpalib.capabilities.heat;

import com.github.lehjr.mpalib.nbt.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.FloatNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MuseHeatItemWrapper extends HeatStorage implements ICapabilityProvider, IHeatWrapper, INBTSerializable<FloatNBT> {
    ItemStack container;
    private final LazyOptional<IHeatStorage> holder = LazyOptional.of(() -> this);

    public MuseHeatItemWrapper(@Nonnull ItemStack container, float capacity) {
        this(container, capacity, capacity, capacity);
    }

    public MuseHeatItemWrapper(@Nonnull ItemStack container, float capacity, float maxTransfer) {
        this(container, capacity, maxTransfer, maxTransfer);
    }

    public MuseHeatItemWrapper(@Nonnull ItemStack container, float capacity, float maxReceive, float maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
        this.container = container;
    }

    /** IItemStackContainerUpdate ----------------------------------------------------------------- */
    @Override
    public void updateFromNBT() {
        heat = Math.min(capacity, NBTUtils.getModularItemFloatOrZero(container, HeatCapability.CURRENT_HEAT));
    }
    @Override
    public float receiveHeat(float heatProvided, boolean simulate) {
        final float heatReceived = super.receiveHeat(heatProvided, simulate);
        if (!simulate && heatReceived != 0) {
            NBTUtils.setModularItemFloatOrRemove(container, HeatCapability.CURRENT_HEAT, heat);
        }
        return heatReceived;
    }

    @Override
    public float extractHeat(float heatRequested, boolean simulate) {
        final float heatExtracted = super.extractHeat(heatRequested, simulate);
        if (!simulate && heatExtracted > 0) {
            NBTUtils.setModularItemFloatOrRemove(container, HeatCapability.CURRENT_HEAT, heat);
        }
        return heatExtracted;
    }

    /** INBTSerializable -------------------------------------------------------------------------- */
    @Override
    public FloatNBT serializeNBT() {
        return FloatNBT.valueOf(heat);
    }

    @Override
    public void deserializeNBT(final FloatNBT nbt) {
        heat = nbt.getFloat();
    }

    /** INBTSerializable<NBTTagFloat> ------------------------------------------------------------ */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return HeatCapability.HEAT.orEmpty(cap, holder);
    }
}