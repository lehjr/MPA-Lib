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

package com.github.lehjr.mpalib.heat;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.capabilities.inventory.modechanging.IModeChangingItem;
import com.github.lehjr.mpalib.capabilities.inventory.modularitem.IModularItem;
import com.github.lehjr.mpalib.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handler for heating and cooling.
 * Note: values can be read on either logical side, but should only be set server side
 */
public class HeatUtils {
    public static final DamageSource overheatDamage = new OverheatDamage();

    public static double getPlayerHeat(EntityPlayer player) {
        double heat = 0;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            heat += getItemHeat(player.getItemStackFromSlot(slot));
        }
        return heat;
    }

    /**
     * Should only be called server side
     */
    public static double getPlayerMaxHeat(EntityPlayer player) {
        AtomicReference<Double> maxHeat = new AtomicReference<>((double) 0);

        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack itemStack = player.getItemStackFromSlot(slot);

            Optional.ofNullable(itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler -> {
                switch (slot.getSlotType()) {
                    case HAND:
                        if (handler instanceof IModeChangingItem) {
                            maxHeat.updateAndGet(v -> new Double((double) (v + getItemMaxHeat(itemStack))));
                        }
                    case ARMOR:
                        if (handler instanceof IModularItem) {
                            maxHeat.updateAndGet(v -> new Double((double) (v + getItemMaxHeat(itemStack))));
                        }
                }
            });

            // Legacy support
            switch (slot.getSlotType()) {
                case HAND:
                    if (itemStack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModeChangingItem) {
                        maxHeat.updateAndGet(v -> new Double((double) (v + getItemMaxHeat(itemStack))));
                    }
                case ARMOR:
                    if (itemStack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModularItem) {
                        maxHeat.updateAndGet(v -> new Double((double) (v + getItemMaxHeat(itemStack))));
                    }
            }
        }

        return maxHeat.get();
    }

    public static double coolPlayer(EntityPlayer player, double coolJoules) {
        if (player.world.isRemote /*|| player.abilities.isCreativeMode */) {
            return 0;
        }

        double coolingLeft = coolJoules;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack stack = player.inventory.getStackInSlot(i);
            if (player.isHandActive() && player.inventory.getCurrentItem() == stack) {
                continue;
            }
            if (coolingLeft > 0) {
                coolingLeft -= coolItem(stack, coolingLeft);
            } else {
                break;
            }
        }
        return coolJoules - coolingLeft;
    }

    /**
     * Should only be called server side
     */
    public static double heatPlayer(EntityPlayer player, double heatJoules) {
        if (player.world.isRemote /*|| player.abilities.isCreativeMode */) {
            return 0;
        }

        NonNullList<ItemStack> items = NonNullList.create();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            items.add(player.getItemStackFromSlot(slot));
        }

        if (player.isHandActive()) {
            items.remove(player.inventory.getCurrentItem());
        }

        double heatLeftToGive = heatJoules;
        // heat player equipped items up to max heat
        for (ItemStack stack : items) {
            double currHeat = getItemHeat(stack);
            double maxHeat = getItemMaxHeat(stack);
            if (currHeat + heatLeftToGive < maxHeat) {
                heatItem(stack, heatLeftToGive);
                return heatJoules;
            } else {
                heatLeftToGive = heatLeftToGive - heatItem(stack, maxHeat - currHeat);
            }
        }

        // apply remaining heat evenly accross the items
        double heatPerStack = heatLeftToGive / items.size();
        for (ItemStack stack : items) {
            heatLeftToGive -= heatItem(stack, heatPerStack);
        }
        return heatJoules - heatLeftToGive;
    }

    public static double getItemMaxHeat(@Nonnull ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(HeatCapability.HEAT, null)).map(h -> h.getMaxHeatStored()).orElse(0D);
    }

    public static double getItemHeat(@Nonnull ItemStack stack) {
        return Optional.ofNullable(stack.getCapability(HeatCapability.HEAT, null)).map(h -> h.getHeatStored()).orElse(0D);
    }

    public static double heatItem(@Nonnull ItemStack stack, double value) {
        return Optional.ofNullable(stack.getCapability(HeatCapability.HEAT, null)).map(h -> h.receiveHeat(value, false)).orElse(0D);
    }

    public static double coolItem(@Nonnull ItemStack stack, double value) {
        return Optional.ofNullable(stack.getCapability(HeatCapability.HEAT, null)).map(h -> h.extractHeat(value, false)).orElse(0D);
    }

    protected static final class OverheatDamage extends DamageSource {
        public OverheatDamage() {
            super(MPALIbConstants.OVERHEAT_DAMAGE);
            this.setFireDamage();
            this.setDamageBypassesArmor();
        }

        public boolean equals(DamageSource other) {
            return other.damageType.equals(this.damageType);
        }
    }
}
