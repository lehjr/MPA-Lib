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

package com.github.lehjr.mpalib.item;

import com.github.lehjr.mpalib.capabilities.inventory.modechanging.IModeChangingItem;
import com.github.lehjr.mpalib.capabilities.inventory.modularitem.IModularItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemUtils {
    /**
     * Scans a specified player's equipment slots for modular items.
     *
     * @param player Entity player that has the equipment slots to scan.
     * @return A List of ItemStacks in the equipment slots which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getModularItemsEquipped(PlayerEntity player) {
        NonNullList<ItemStack> modulars = NonNullList.create();
        for (EquipmentSlotType slot : EquipmentSlotType.values()) {
            ItemStack itemStack = player.getItemStackFromSlot(slot);

            itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler-> {
                switch(slot.getSlotType()) {
                    case HAND:
                        if (handler instanceof IModeChangingItem)
                            modulars.add(itemStack);
                    case ARMOR:
                        if (handler instanceof IModularItem)
                            modulars.add(itemStack);
                }
            });

        }
        return modulars;
    }

    /**
     * Scans a specified player's inventory for modular items.
     *
     * @param player Entity player that has the inventory to scan.
     * @return A List of ItemStacks in the playuer's inventory which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getModularItemsInInventory(PlayerEntity player) {
        return getModularItemsInInventory(player.inventory);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of ItemStacks in the inventory which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getModularItemsInInventory(IInventory inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler -> {
                if (handler instanceof IModularItem)
                    stacks.add(itemStack);
            });
        }
        return stacks;
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param player's whose inventory to scan.
     * @return A List of inventory slots containing an IModularItem
     */
    public static List<Integer> getModularItemSlotsEquiped(PlayerEntity player) {
        // mainhand ... a hotbar number
        // offhand .... 40
        // head ....... 39
        // chest ...... 38
        // legs ....... 37
        // feet ....... 36

        ArrayList<Integer> slots = new ArrayList<>();
        player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->{
            if (handler instanceof IModeChangingItem)
                slots.add(player.inventory.currentItem);
        });

        for (int i = 36; i < player.inventory.getSizeInventory(); i++) {
            player.inventory.getStackInSlot(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(handler ->{
                if (handler instanceof IModularItem)
                    slots.add(player.inventory.currentItem);
            });
        }
        return slots;
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param player's whose inventory to scan.
     * @return A List of inventory slots containing an IModularItem
     */
    public static List<Integer> getModularItemSlotsInInventory(PlayerEntity player) {
        ArrayList<Integer> slots = new ArrayList<>();
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            int finalI = i;
            player.inventory.getStackInSlot(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .ifPresent(handler -> {
                        if (handler instanceof IModularItem)
                            slots.add(finalI);
                    });
        }
        return slots;
    }

    /**
     * Checks if the player has a copy of all of the items in
     * workingUpgradeCost.
     *
     * @param workingUpgradeCost
     * @param inventory
     * @return
     */
    @Deprecated // Install costs should be abandoned as modules are now items
    public static boolean hasInInventory(List<ItemStack> workingUpgradeCost, PlayerInventory inventory) {
        for (ItemStack stackInCost : workingUpgradeCost) {
            int found = 0;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (ItemStack.areItemStacksEqual(stackInInventory, stackInCost)) {
                    found += stackInInventory.getCount();
                }
            }
            if (found < stackInCost.getCount()) {
                return false;
            }
        }
        return true;
    }
}