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

package com.github.lehjr.mpalib.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ItemUtils {
    /**
     * Helper function for making recipes. Returns a copy of the itemstack with
     * the specified stacksize.
     *
     * @param stack  Itemstack to copy
     * @param number New Stacksize
     * @return A new itemstack with the specified properties
     */
    public static ItemStack copyAndResize(@Nonnull ItemStack stack, int number) {
        ItemStack copy = stack.copy();
        copy.setCount(number);
        return copy;
    }

    public static List<Integer> deleteFromInventory(NonNullList<ItemStack> cost, InventoryPlayer inventory) {
        List<Integer> slots = new LinkedList<>();
        for (ItemStack stackInCost : cost) {
            int remaining = stackInCost.getCount();
            for (int i = 0; i < inventory.getSizeInventory() && remaining > 0; i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    int numToTake = Math.min(stackInInventory.getCount(), remaining);
                    stackInInventory.setCount(stackInInventory.getCount() - numToTake);
                    remaining -= numToTake;
                    if (stackInInventory.getCount() == 0) {
                        inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    slots.add(i);
                }
            }
        }
        return slots;
    }

    public static boolean isSameItem(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty() || stack1.getItem() != stack2.getItem()) {
            return false;
        } else
            return !((!stack1.isItemStackDamageable())
                    && (stack1.getItemDamage() != stack2.getItemDamage()));
    }
//
//    /**
//     * Scans a specified player's equipment slots for modular items.
//     *
//     * @param player Entity player that has the equipment slots to scan.
//     * @return A List of ItemStacks in the equipment slots which implement
//     * IModularItem
//     */
//    public static NonNullList<ItemStack> getModularItemsEquipped(EntityPlayer player) {
//        NonNullList<ItemStack> modulars = NonNullList.create();
//        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
//            ItemStack itemStack = player.getItemStackFromSlot(slot);
//
//            Optional.ofNullable(itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler-> {
//                switch(slot.getSlotType()) {
//                    case HAND:
//                        if (handler instanceof IModeChangingItem)
//                            modulars.add(itemStack);
//                    case ARMOR:
//                        if (handler instanceof IModularItem)
//                            modulars.add(itemStack);
//                }
//            });
//
//        }
//        return modulars;
//    }

    /**
     * Scans a specified player's equipment slots for modular items.
     *
     * @param player Entity player that has the equipment slots to scan.
     * @return A List of ItemStacks in the equipment slots which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getLegacyModularItemsEquipped(EntityPlayer player) {
        NonNullList<ItemStack> modulars = NonNullList.create();
        for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack itemStack = player.getItemStackFromSlot(slot);

            switch(slot.getSlotType()) {
                case HAND:
                    if (itemStack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModeChangingItem )
                        modulars.add(itemStack);
                case ARMOR:
                    if (itemStack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModularItem)
                        modulars.add(itemStack);
            }
        }
        return modulars;
    }
//
//    /**
//     * Scans a specified player's inventory for modular items.
//     *
//     * @param player Entity player that has the inventory to scan.
//     * @return A List of ItemStacks in the playuer's inventory which implement
//     * IModularItem
//     */
//    public static NonNullList<ItemStack> getModularItemsInInventory(EntityPlayer player) {
//        return getModularItemsInInventory(player.inventory);
//    }
//
//    /**
//     * Scans a specified inventory for modular items.
//     *
//     * @param inv IInventory to scan.
//     * @return A List of ItemStacks in the inventory which implement
//     * IModularItem
//     */
//    public static NonNullList<ItemStack> getModularItemsInInventory(IInventory inv) {
//        NonNullList<ItemStack> stacks = NonNullList.create();
//
//        for (int i = 0; i < inv.getSizeInventory(); i++) {
//            ItemStack itemStack = inv.getStackInSlot(i);
//            Optional.ofNullable(itemStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler -> {
//                if (handler instanceof IModularItem)
//                    stacks.add(itemStack);
//            });
//        }
//        return stacks;
//    }

    /**
     * Scans a specified player's inventory for modular items.
     *
     * @param player Entity player that has the inventory to scan.
     * @return A List of ItemStacks in the playuer's inventory which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getLegacyModularItemsInInventory(EntityPlayer player) {
        return getLegacyModularItemsInInventory(player.inventory);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of ItemStacks in the inventory which implement
     * IModularItem
     */
    public static NonNullList<ItemStack> getLegacyModularItemsInInventory(IInventory inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack itemStack = inv.getStackInSlot(i);
            if (itemStack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModularItem) {
                stacks.add(itemStack);
            }
        }
        return stacks;
    }
//
//    /**
//     * Scans a specified inventory for modular items.
//     *
//     * @param player's whose inventory to scan.
//     * @return A List of inventory slots containing an IModularItem
//     */
//    public static List<Integer> getModularItemSlotsEquiped(EntityPlayer player) {
//        // mainhand ... a hotbar number
//        // offhand .... 40
//        // head ....... 39
//        // chest ...... 38
//        // legs ....... 37
//        // feet ....... 36
//
//        ArrayList<Integer> slots = new ArrayList<>();
//        Optional.ofNullable(player.getHeldItemMainhand().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler ->{
//            if (handler instanceof IModeChangingItem) {
//                slots.add(player.inventory.currentItem);
//            }
//        });
//
//        for (int i = 36; i < player.inventory.getSizeInventory(); i++) {
//            Optional.ofNullable(player.inventory.getStackInSlot(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler ->{
//                if (handler instanceof IModularItem) {
//                    slots.add(player.inventory.currentItem);
//                }
//            });
//        }
//        return slots;
//    }

//    /**
//     * Scans a specified inventory for modular items.
//     *
//     * @param player's whose inventory to scan.
//     * @return A List of inventory slots containing an IModularItem
//     */
//    public static List<Integer> getModularItemSlotsInInventory(EntityPlayer player) {
//        ArrayList<Integer> slots = new ArrayList<>();
//        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
//            int finalI = i;
//            Optional.ofNullable(player.inventory.getStackInSlot(i).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
//                    .ifPresent(handler -> {
//                        if (handler instanceof IModularItem)
//                            slots.add(finalI);
//                    });
//        }
//        return slots;
//    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param player's whose inventory to scan.
     * @return A List of inventory slots containing an IModularItem
     */
    public static List<Integer> getLegacyModularItemSlotsInInventory(EntityPlayer player) {
        ArrayList<Integer> slots = new ArrayList<>();
        ItemStack stack;
        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            stack = player.inventory.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof com.github.lehjr.mpalib.legacy.item.IModularItem) {
                slots.add(i);
            }
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
    public static boolean hasInInventory(List<ItemStack> workingUpgradeCost, InventoryPlayer inventory) {
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