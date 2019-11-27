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

package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.energy.ElectricAdapterManager;
import com.github.lehjr.mpalib.energy.adapter.IElectricAdapter;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum MPALibConfig {
    INSTANCE;

    private static MPALibServerSettings serverSettings;

    public static boolean useSounds() {
        return MPALibSettings.general.useSounds;
    }

    public static boolean isDebugging() {
        return MPALibSettings.general.isDebugging;
    }

    public static boolean useFOVFix() {
        return MPALibSettings.general.useFOVFix;
    }

    public static boolean fovFixDefaultState() {
        return MPALibSettings.general.fovFixDefaultState;
    }

    /**
     * Energy ------------------------------------------------------------------------------------
     */
    // 1 RF = 0.1 MJ (Mekanism)
    public static double getMekRatio() {
        return getServerSettings() != null ? getServerSettings().mekRatio : MPALibSettings.general.mekRatio;
    }

    // 1 RF = 0.25 EU
    public static double getIC2Ratio() {
        return getServerSettings() != null ? getServerSettings().ic2Ratio : MPALibSettings.general.ic2Ratio;
    }

    // (Refined Storage) 1 RS = 1 RF
    public static double getRSRatio() {
        return getServerSettings() != null ? getServerSettings().rsRatio : MPALibSettings.general.rsRatio;
    }

    // 1 RF = 0.5 AE
    public static double getAE2Ratio() {
        return getServerSettings() != null ? getServerSettings().ae2Ratio : MPALibSettings.general.ae2Ratio;
    }

    public static int getTier1MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier1 : MPALibSettings.general.maxTier1;
    }

    public static int getTier2MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier2 : MPALibSettings.general.maxTier2;
    }

    public static int getTier3MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier3 : MPALibSettings.general.maxTier3;
    }

    public static int getTier4MaxRF() {
        return getServerSettings() != null ? getServerSettings().maxTier4 : MPALibSettings.general.maxTier4;
    }

    /**
     * Used for getting the tier of an ItemStack. Used for various functions
     */
    public static int getTierForItem(@Nonnull ItemStack itemStack) {
        IElectricAdapter adapter = ElectricAdapterManager.INSTANCE.wrap(itemStack, true);
        if (adapter != null) {
            int maxEnergy = adapter.getMaxEnergyStored();
            if (maxEnergy <= getTier1MaxRF())
                return 1;
            else if (maxEnergy <= getTier2MaxRF())
                return 2;
            else if (maxEnergy <= getTier3MaxRF())
                return 3;
            else if (maxEnergy <= getTier4MaxRF())
                return 4;
            return 5;
        }
        return 0;
    }

    @Nullable
    public static final MPALibServerSettings getServerSettings() {
        return INSTANCE.serverSettings;
    }

    public static void setServerSettings(@Nullable final MPALibServerSettings serverSettings) {
        INSTANCE.serverSettings = serverSettings;
    }
}