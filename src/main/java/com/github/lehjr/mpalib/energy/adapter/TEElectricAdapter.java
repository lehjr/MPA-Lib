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

package com.github.lehjr.mpalib.energy.adapter;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class TEElectricAdapter implements IElectricAdapter {
    private final ItemStack stack;
    private final IEnergyContainerItem item;

    public TEElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IEnergyContainerItem) stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IEnergyContainerItem item() {
        return this.item;
    }

    @Nullable
    @Override
    public IElectricAdapter wrap() {
        return null;
    }

    @Override
    public int getEnergyStored() {
        return this.item().getEnergyStored(this.stack);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.item().getMaxEnergyStored(this.stack);
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        return this.item().extractEnergy(this.stack, requested, simulate);
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return this.item().receiveEnergy(this.stack, provided, simulate);
    }
}