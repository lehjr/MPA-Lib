package com.github.lehjr.mpalib.energy.adapter;

import com.github.lehjr.mpalib.energy.ElectricConversions;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public class IC2ElectricAdapter implements IElectricAdapter {
    private final ItemStack stack;
    private final IElectricItem item;

    public IC2ElectricAdapter(final ItemStack stack) {
        this.stack = stack;
        this.item = (IElectricItem) stack.getItem();
    }

    public ItemStack stack() {
        return this.stack;
    }

    public IElectricItem item() {
        return this.item;
    }

    public int getTier() {
        return this.item.getTier(this.stack);
    }

    @Nullable
    @Override
    public IElectricAdapter wrap() {
        return null;
    }

    @Override
    public int getEnergyStored() {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.getCharge(this.stack));
    }

    @Override
    public int getMaxEnergyStored() {
        return ElectricConversions.museEnergyFromEU(this.item.getMaxCharge(this.stack));
    }

    @Override
    public int extractEnergy(int requested, boolean simulate) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.discharge(this.stack, ElectricConversions.museEnergyToEU(requested), this.getTier(), true, false, simulate));
    }

    @Override
    public int receiveEnergy(int provided, boolean simulate) {
        return ElectricConversions.museEnergyFromEU(ElectricItem.manager.charge(this.stack, ElectricConversions.museEnergyToEU(provided), this.getTier(), true, simulate));
    }
}