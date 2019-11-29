/*
 * MPA-Lib (Formerly known as Numina)
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
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MPALibHeatItemWrapper extends HeatStorage implements ICapabilityProvider, IHeatWrapper, INBTSerializable<NBTTagDouble> {
    ItemStack container;
    private final IHeatStorage holder = this;

    public MPALibHeatItemWrapper(@Nonnull ItemStack container, double capacity) {
        this(container, capacity, capacity, capacity);
    }

    public MPALibHeatItemWrapper(@Nonnull ItemStack container, double capacity, double maxTransfer) {
        this(container, capacity, maxTransfer, maxTransfer);
    }

    public MPALibHeatItemWrapper(@Nonnull ItemStack container, double capacity, double maxReceive, double maxExtract) {
        super(capacity, maxReceive, maxExtract, 0);
        this.container = container;
    }

    /** IItemStackContainerUpdate ----------------------------------------------------------------- */
    @Override
    public void updateFromNBT() {
        heat = Math.min(capacity, NBTUtils.getModularItemDoubleOrZero(container, HeatCapability.CURRENT_HEAT));
    }
    @Override
    public double receiveHeat(double heatProvided, boolean simulate) {
        final double heatReceived = super.receiveHeat(heatProvided, simulate);
        if (!simulate && heatReceived != 0) {
            NBTUtils.setModularItemDoubleOrRemove(container, HeatCapability.CURRENT_HEAT, heat);
        }
        return heatReceived;
    }

    @Override
    public double extractHeat(double heatRequested, boolean simulate) {
        final double heatExtracted = super.extractHeat(heatRequested, simulate);
        if (!simulate && heatExtracted > 0) {
            NBTUtils.setModularItemDoubleOrRemove(container, HeatCapability.CURRENT_HEAT, heat);
        }
        return heatExtracted;
    }

    /** NBTBaseSerializable -------------------------------------------------------------------------- */
    @Override
    public NBTTagDouble serializeNBT() {
        return new NBTTagDouble(heat);
    }

    @Override
    public void deserializeNBT(final NBTTagDouble nbt) {
        heat = nbt.getDouble();
    }

    /** NBTBaseSerializable<NBTTagDouble> ------------------------------------------------------------ */
     @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == HeatCapability.HEAT;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == HeatCapability.HEAT ? (T) holder : null;
    }
}