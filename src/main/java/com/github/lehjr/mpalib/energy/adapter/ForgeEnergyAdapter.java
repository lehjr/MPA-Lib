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

import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

public class ForgeEnergyAdapter implements IElectricAdapter {
    private ItemStack itemStack;
    private Optional<IEnergyStorage> energyStorage;

    public ForgeEnergyAdapter(@Nonnull ItemStack itemStack) {
        this.itemStack = itemStack;
        energyStorage = Optional.of(itemStack.getCapability(CapabilityEnergy.ENERGY, null));
    }

    @Nullable
    @Override
    public IElectricAdapter wrap() {
        return energyStorage.isPresent() ? this: null;
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.map(m -> m.getEnergyStored()).orElse(0);
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.map(m -> m.getMaxEnergyStored()).orElse(0);
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        if (requested == 0)
            return 0;
        return energyStorage.map(m -> m.extractEnergy(requested, simulate)).orElse(0);
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return energyStorage.map(m -> m.receiveEnergy(provided, simulate)).orElse(0);
    }
}