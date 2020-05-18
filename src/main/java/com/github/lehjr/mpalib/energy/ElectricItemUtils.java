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
import com.github.lehjr.mpalib.energy.adapter.IElectricAdapter;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;

public class ElectricItemUtils {
    /**
     * applies a given charge to the emulated tool. Used for simulating a used charge from the tool as it would normally be used
     */
    public static int chargeEmulatedToolFromPlayerEnergy(LivingEntity entity, @Nonnull ItemStack emulatedTool) {
        if (entity.world.isRemote) {
            return 0;
        }

        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(emulatedTool, false);
        if (adapter == null) {
            return 0;
        }

        int maxCharge = adapter.getMaxEnergyStored();
        int charge = adapter.getEnergyStored();

        if (maxCharge == charge) {
            return 0;
        }

        int chargeAmount;
        int playerEnergy = getPlayerEnergy(entity);
        if (playerEnergy > (maxCharge - charge)) {
            adapter.receiveEnergy(maxCharge - charge, false);
            chargeAmount = drainPlayerEnergy(entity, maxCharge - charge);
        } else {
            adapter.receiveEnergy(playerEnergy, false);
            chargeAmount = drainPlayerEnergy(entity, playerEnergy);
        }
        return chargeAmount;
    }

    /**
     * returns the sum of the energy of the entity's equipped items
     *
     * Note here we filter out foreign items so the entity available/ usableenergy isn't wrong
     */
    public static int getPlayerEnergy(LivingEntity entity) {
        int avail = 0;

        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(entity.getItemStackFromSlot(slot), true);
            if (adapter == null) {
                continue;
            }
            avail += adapter.getEnergyStored();
        }
        return avail;
    }

    /**
     * returns the total possible amount of energy the entity's equipped items can hold
     *
     * Note here we filter out foreign items so the entity available/ usableenergy isn't wrong
     */
    public static int getMaxPlayerEnergy(LivingEntity entity) {
        int avail = 0;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(entity.getItemStackFromSlot(slot), true);
            if (adapter == null) {
                continue;
            }
            avail += adapter.getMaxEnergyStored();
        }
        return avail;
    }

    /**
     * returns the total amount of energy drained from the entity's equipped items
     *
     * Note that charging held items while in use causes issues so they are skipped
     */
    public static int drainPlayerEnergy(LivingEntity entity, int drainAmount) {
        if (entity.world.isRemote || (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.isCreativeMode)) {
            return 0;
        }
        int drainleft = drainAmount;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {

            // FIXME: do we still have to filter out items in use with the ~canContinueUsing method being set to true?
//            if (slot.getSlotType() == EquipmentSlotType.Group.HAND) {
//                if (slot == EquipmentSlotType.MAINHAND && entity.getActiveHand() == Hand.MAIN_HAND) {
//                    continue;
//                } else if  (slot == EquipmentSlotType.OFFHAND && entity.getActiveHand() == Hand.OFF_HAND) {
//                    continue;
//                }
//            }
            ItemStack stack = entity.getItemStackFromSlot(slot);
            // check if the tool is a modular item. If not, skip it.
            if (!stack.isEmpty() && stack.getItem() instanceof ToolItem) {
                if (stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).map(iItemHandler ->
                        !(iItemHandler instanceof IModularItem)).orElse(true)){
                    continue;
                }
            }

            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(entity.getItemStackFromSlot(slot), true);
            if (adapter == null) {
                continue;
            }

            if (drainleft > 0) {
                drainleft = drainleft - adapter.extractEnergy(drainleft, false);
            } else {
                break;
            }
        }
        return drainAmount - drainleft;
    }

    /**
     * returns the total amount of energy given to a entity's equipped items
     *
     * Note that charging held items while in use causes issues so they are skipped
     */
    public static int givePlayerEnergy(LivingEntity entity, int rfToGive) {
        int rfLeft = rfToGive;
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
//            if (slot.getSlotType() == EquipmentSlotType.Group.HAND) {
//                if (slot == EquipmentSlotType.MAINHAND && entity.getActiveHand() == Hand.MAIN_HAND) {
//                    continue;
//                } else if  (slot == EquipmentSlotType.OFFHAND && entity.getActiveHand() == Hand.OFF_HAND) {
//                    continue;
//                }
//            }

            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(entity.getItemStackFromSlot(slot), false);
            if (adapter == null)
                continue;

            if (adapter != null) {
                if (rfLeft > 0) {
                    rfLeft = rfLeft - adapter.receiveEnergy(rfLeft, false);
                } else
                    break;
            }
        }
        return rfToGive - rfLeft;
    }

    /**
     * returns the energy an itemStack has
     */
    public static int getItemEnergy(@Nonnull ItemStack itemStack) {
        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(itemStack, false);
        if (adapter != null) {
            return adapter.getEnergyStored();
        }
        return 0;
    }

    /**
     * returns total possible energy an itemStack can hold
     */
    public static int getMaxItemEnergy(@Nonnull ItemStack itemStack) {
        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(itemStack, false);
        if (adapter != null) {
            return adapter.getEnergyStored();
        }
        return 0;
    }

    public static int chargeItem(@Nonnull ItemStack itemStack, int chargeAmount) {
        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(itemStack, false);
        if (adapter != null) {
            return adapter.receiveEnergy(chargeAmount, false);
        }
        return 0;
    }
}