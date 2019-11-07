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

package com.github.lehjr.mpalib.energy;

import com.github.lehjr.mpalib.capabilities.inventory.modularitem.IModularItem;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.energy.adapter.BlackList;
import com.github.lehjr.mpalib.energy.adapter.ForgeEnergyAdapter;
import com.github.lehjr.mpalib.energy.adapter.IElectricAdapter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Universal power system handler. You can create and add your own wrapper if needed.
 *
 * Notes:
 * Charging and discharging should only be done server side.
 * Charging any item is usually fine, but discharging some items, especially tools usually gets users mad.
 *
 */

public enum ElectricAdapterManager {
    INSTANCE;

    List<Function<ItemStack, IElectricAdapter>> adapters = new ArrayList<Function<ItemStack, IElectricAdapter>>() {{
       add(ForgeEnergyAdapter::new);
    }};

    public IElectricAdapter wrap(@Nonnull ItemStack itemStack, boolean excludeForeignItems) {
        String itemMod = itemStack.getItem().getRegistryName().getNamespace();
        if (BlackList.INSTANCE.blacklistModIds.contains(itemMod)) {
            return null;
        }

        /*
            when excluding foreign items, only modular items and modules can be charged/discharged. This SHOULD be enough to filter out foreign tools
        */
        if ((excludeForeignItems &&
                (Optional.ofNullable(itemStack.getCapability(PowerModuleCapability.POWER_MODULE, null)).isPresent() ||
                        Optional.ofNullable(itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).map(iH-> iH instanceof IModularItem).orElse(false))) ||
                !excludeForeignItems) {
            for (Function<ItemStack, IElectricAdapter> adapter : adapters) {

                // yeah, kinda stupid... but, apply actually creates the instance, and wrap checks if the energy handler is present.
                if (adapter.apply(itemStack).wrap() != null) {
                    return adapter.apply(itemStack).wrap();
                }
            }
        }
        return null;
    }

    public void addAdapter(Function<ItemStack, IElectricAdapter> adapter) {
        if (!adapters.contains(adapter)) {
            adapters.add(adapter);
        }
    }
}