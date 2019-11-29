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

package com.github.lehjr.mpalib.energy.adapter;

import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.api.ITeslaHolder;
import net.darkhax.tesla.api.ITeslaProducer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class TeslaEnergyAdapter implements IElectricAdapter {
    private final ItemStack itemStack;
    private final ITeslaHolder holder;
    private final ITeslaConsumer consumer;
    private final ITeslaProducer producer;

    public TeslaEnergyAdapter(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.consumer = this.itemStack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
        this.holder = itemStack.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null);
        this.producer = itemStack.getCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null);
    }

    @Nullable
    @Override
    public IElectricAdapter wrap() {
        return null;
    }

    @Override
    public int getEnergyStored() {
        return holder != null ? (int) holder.getStoredPower() : 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return holder != null ? (int) holder.getCapacity() : 0;
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        return producer != null ? (int) producer.takePower(requested, simulate) : 0;
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return consumer != null ? (int) consumer.givePower(provided, simulate) : 0;
    }
}