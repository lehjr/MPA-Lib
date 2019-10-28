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
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import java.util.Optional;

public class ElectricItemUtils {
    /**
     * applies a given charge to the emulated tool. Used for simulating a used charge from the tool as it would normally be used
     */
    public static int chargeEmulatedToolFromPlayerEnergy(EntityPlayer player, @Nonnull ItemStack emulatedTool) {
        if (player.world.isRemote) {
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
        int playerEnergy = getPlayerEnergy(player);
        if (playerEnergy > (maxCharge - charge)) {
            adapter.receiveEnergy(maxCharge - charge, false);
            chargeAmount = drainPlayerEnergy(player, maxCharge - charge);
        } else {
            adapter.receiveEnergy(playerEnergy, false);
            chargeAmount = drainPlayerEnergy(player, playerEnergy);
        }
        return chargeAmount;
    }

    /**
     * returns the sum of the energy of the player's equipped items
     *
     * Note here we filter out foreign items so the player available/ usableenergy isn't wrong
     */
    public static int getPlayerEnergy(EntityPlayer player) {
        int avail = 0;

        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(player.getItemStackFromSlot(slot), true);
            if (adapter == null) {
                continue;
            }
            avail += adapter.getEnergyStored();
        }
        return avail;
    }

    /**
     * returns the total possible amount of energy the player's equipped items can hold
     *
     * Note here we filter out foreign items so the player available/ usableenergy isn't wrong
     */
    public static int getMaxPlayerEnergy(EntityPlayer player) {
        int avail = 0;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(player.getItemStackFromSlot(slot), true);
            if (adapter == null) {
                continue;
            }
            avail += adapter.getMaxEnergyStored();
        }
        return avail;
    }

    /**
     * returns the total amount of energy drained from the player's equipped items
     *
     * Note that charging held items while in use causes issues so they are skipped
     */
    public static int drainPlayerEnergy(EntityPlayer player, int drainAmount) {
        if (player.world.isRemote || player.capabilities.isCreativeMode) {
            return 0;
        }
        int drainleft = drainAmount;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {

            // FIXME: do we still have to filter out items in use with the ~canContinueUsing method being set to true?
//            if (slot.getSlotType() == EntityEquipmentSlot.Group.HAND) {
//                if (slot == EntityEquipmentSlot.MAINHAND && player.getActiveHand() == Hand.MAIN_HAND) {
//                    continue;
//                } else if  (slot == EntityEquipmentSlot.OFFHAND && player.getActiveHand() == Hand.OFF_HAND) {
//                    continue;
//                }
//            }
            ItemStack stack = player.getItemStackFromSlot(slot);
            // check if the tool is a modular item. If not, skip it.
            if (!stack.isEmpty() && stack.getItem() instanceof ItemTool) {
                if (Optional.of(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).map(iItemHandler ->
                        !(iItemHandler instanceof IModularItem)).orElse(true)){
                    continue;
                }
            }

            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(player.getItemStackFromSlot(slot), true);
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
     * returns the total amount of energy given to a player's equipped items
     *
     * Note that charging held items while in use causes issues so they are skipped
     */
    public static int givePlayerEnergy(EntityPlayer player, int rfToGive) {
        int rfLeft = rfToGive;
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
//            if (slot.getSlotType() == EntityEquipmentSlot.Group.HAND) {
//                if (slot == EntityEquipmentSlot.MAINHAND && player.getActiveHand() == Hand.MAIN_HAND) {
//                    continue;
//                } else if  (slot == EntityEquipmentSlot.OFFHAND && player.getActiveHand() == Hand.OFF_HAND) {
//                    continue;
//                }
//            }

            IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(player.getItemStackFromSlot(slot), false);
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