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