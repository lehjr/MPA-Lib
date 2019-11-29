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

package com.github.lehjr.mpalib.energy;

import com.github.lehjr.mpalib.config.MPALibConfig;
import net.minecraft.item.ItemStack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:51 AM, 4/28/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public class ElectricConversions {
    /**
     * Industrialcraft 2 --------------------------------------------------------------------------
     */
    public static final String IC2_TIER = "IC2 Tier";
    static MPALibConfig config = MPALibConfig.INSTANCE;

    public static int getTier(final ItemStack stack) {
        return MPALibConfig.INSTANCE.getTierForItem(stack);
    }

    public static double forgeEnergyToEU(final double forgeEnergy) {
        return forgeEnergy / config.getIC2Ratio();
    }

    public static int forgeEnergyFromEU(final double eu) {
        return (int) Math.round(eu * config.getIC2Ratio());
    }

    /**
     * Mekanism ------------------------------------------------------------------------------------
     */
    public static double forgeEnergyToMek(final double forgeEnergy) { // no current conversion rate
        return Math.ceil(forgeEnergy / config.getMekRatio());
    }

    public static int forgeEnergyFromMek(final double mj) { // no current conversion rate
        return (int) Math.round(mj * config.getMekRatio());
    }

    /**
     * Applied Energistics 2 ----------------------------------------------------------------------
     */
    public static double forgeEnergyFromAE(final double ae) {
        return ae * config.getAE2Ratio();
    }

    public static double forgeEnergyToAE(final double forgeEnergy) {
        return forgeEnergy / config.getAE2Ratio();
    }
}