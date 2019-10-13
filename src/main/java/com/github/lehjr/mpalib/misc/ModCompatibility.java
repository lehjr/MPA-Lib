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

package com.github.lehjr.mpalib.misc;

import net.minecraftforge.fml.ModList;

public class ModCompatibility {
    public static boolean isRFAPILoaded() {
        return ModList.get().isLoaded("redstoneflux");
    }

    public static boolean isTeslaLoaded() {
        return ModList.get().isLoaded("tesla");
    }

    public static boolean isCOFHCoreLoaded() {
//        return ModAPIManager.INSTANCE.hasAPI("cofhcore");
        return ModList.get().isLoaded("cofhcore");
    }

    public static boolean isCOFHLibLoaded() {
        return ModList.get().isLoaded("cofhlib");
    }

    public static boolean isThermalExpansionLoaded() {
        return ModList.get().isLoaded("thermalexpansion") && ModList.get().isLoaded("thermalfoundation");
    }

    public static boolean isEnderCoreLoaded() {
        return ModList.get().isLoaded("endercore");
    }

    public static boolean isEnderIOLoaded() {
        return ModList.get().isLoaded("enderio");
    }

    public static boolean isTechRebornLoaded() {
        return ModList.get().isLoaded("techreborn");
    }

    public static boolean isGregTechLoaded() {
        return ModList.get().isLoaded("gregtech");
    }

    // Industrialcraft common
    public static boolean isIndustrialCraftLoaded() {
        return ModList.get().isLoaded("ic2");
    }

    public static final boolean isIndustrialCraftExpLoaded() {
//        if (!isIndustrialCraftLoaded())
//            return false;
//
//        List<ModInfo> list = ModList.get().getMods();
//        for (ModContainer container : list) {
//            if (container.getModId().toLowerCase().equals("ic2")) {
//                if (container.getModId().equals("IndustrialCraft 2"))
//                    return true;
//                return false;
//            }
//        }
        return false;
    }

    // Industrialcraft 2 classic (note redundant code is intentional for "just in case")
    public static final boolean isIndustrialCraftClassicLoaded() {
//        if (!isIndustrialCraftLoaded())
//            return false;
//
//        List<ModContainer> list = Loader.instance().getModList();
//        for (ModContainer container : list) {
//            if (container.getModId().toLowerCase().equals("ic2")) {
//                if (container.getgetName().equals("Industrial Craft Classic"))
//                    return true;
//                return false;
//            }
//        }
        return false;
    }

    public static boolean isThaumCraftLoaded() {
        return ModList.get().isLoaded("thaumcraft");
    }

    public static boolean isGalacticraftLoaded() {
        return ModList.get().isLoaded("galacticraftcore");
    }

    public static boolean isForestryLoaded() {
        return ModList.get().isLoaded("forestry");
    }

    public static boolean isChiselLoaded() {
        return ModList.get().isLoaded("chisel");
    }

    public static boolean isAppengLoaded() {
        return ModList.get().isLoaded("appliedenergistics2");
    }

    public static boolean isExtraCellsLoaded() {
        return ModList.get().isLoaded("extracells");
    }

    public static boolean isMFRLoaded() {
        return ModList.get().isLoaded("mineFactoryreloaded");
    }

    public static boolean isRailcraftLoaded() {
        return ModList.get().isLoaded("railcraft");
    }

    public static boolean isCompactMachinesLoaded() {
        return ModList.get().isLoaded("cm2");
    }

    public static boolean isRenderPlayerAPILoaded() {
        return ModList.get().isLoaded("RenderPlayerAPI".toLowerCase());
    }

    public static boolean isRefinedStorageLoaded() {
        return ModList.get().isLoaded("refinedstorage");
    }

    public static boolean isScannableLoaded() {
        return ModList.get().isLoaded("scannable");
    }

    public static boolean isWirelessCraftingGridLoaded() {
        return ModList.get().isLoaded("wcg");
    }

    public static boolean isMekanismLoaded() {
        return ModList.get().isLoaded("mekanism");
    }
}