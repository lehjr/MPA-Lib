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

package com.github.lehjr.mpalib.legacy.energy;

import cofh.redstoneflux.api.IEnergyContainerItem;
import com.github.lehjr.mpalib.energy.ElectricConversions;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:12 PM, 4/20/13
 * <p>
 * Ported to Java by lehjr on 11/3/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "ic2", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "ic2", striprefs = true)
})
public interface IElectricItem
        extends
        IEnergyContainerItem,
        ISpecialElectricItem,
        IElectricItemManager {

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    default IElectricItemManager getManager(ItemStack stack) {
        return this;
    }

    default double getTransferLimit(ItemStack itemStack) {
        if (itemStack.isEmpty() || !(itemStack.getItem() instanceof IElectricItem))
            return 0;
        IElectricItem iMuseElectricItem = (IElectricItem) itemStack.getItem();
        return ElectricConversions.forgeEnergyToEU(iMuseElectricItem.getMaxEnergyStored(itemStack) * 0.75);
    }

    @Override
    default double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        // some machines use "Inifinity" and it converts to "-1" This negates their weird effect.
        if (amount == Double.POSITIVE_INFINITY || amount == Double.NEGATIVE_INFINITY)
            amount = Integer.MAX_VALUE * 0.25D;

        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.forgeEnergyFromEU(amount) : ElectricConversions.forgeEnergyFromEU(getTransferLimit(itemStack));
        transfer = Math.abs(transfer);

        return ElectricConversions.forgeEnergyToEU(receiveEnergy(itemStack, transfer, simulate));
    }

    @Override
    default double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        // some machines use "Inifinity" and it converts to "-1" This negates their weird effect.
        if (amount == Double.POSITIVE_INFINITY || amount == Double.NEGATIVE_INFINITY)
            amount = Integer.MAX_VALUE * 0.25D;

        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.forgeEnergyFromEU(amount) : ElectricConversions.forgeEnergyFromEU(getTransferLimit(itemStack));
        transfer = Math.abs(transfer);

        return ElectricConversions.forgeEnergyToEU(extractEnergy(itemStack, transfer, simulate));
    }

    @Override
    default double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.forgeEnergyToEU(getMaxEnergyStored(itemStack));
    }

    @Override
    default double getCharge(ItemStack itemStack) {
        return ElectricConversions.forgeEnergyToEU(getEnergyStored(itemStack));
    }


    @Override
    default boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.forgeEnergyFromEU(amount) < getEnergyStored(itemStack);
    }

    @Override
    default boolean use(ItemStack itemStack, double amount, EntityLivingBase entityLivingBase) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.forgeEnergyToEU(amount), entityLivingBase);
    }

    @Override
    default void chargeFromArmor(ItemStack itemStack, EntityLivingBase entityLivingBase) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entityLivingBase);
    }

    @Override
    default String getToolTip(ItemStack itemStack) {
        return null;
    }

    @Override
    default int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    default int receiveEnergy(ItemStack container, int maxExtract, boolean simulate) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(maxExtract, simulate) : 0;
    }

    @Override
    default int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    default int getEnergyStored(ItemStack container) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    default int getMaxEnergyStored(ItemStack container) {
        IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getMaxEnergyStored() : 0;
    }
}