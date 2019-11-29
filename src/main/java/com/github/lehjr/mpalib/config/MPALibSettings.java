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

package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.basemod.MPALibLogger;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = MPALIbConstants.MODID, name = MPALIbConstants.CONFIG_FILE)
public class MPALibSettings {

    public static General general = new General();
    public static class General {
        @Config.LangKey(MPALIbConstants.CONFIG_USE_FOV_FIX)
        @Config.Comment("Ignore speed boosts for field of view")
        public boolean useFOVFix = true;


        @Config.LangKey(MPALIbConstants.CONFIG_FOV_FIX_DEAULT_STATE)
        @Config.Comment("Default state of FOVfix on login (enabled = true, disabled = false)")
        public boolean fovFixDefaultState = true;


        @Config.LangKey(MPALIbConstants.CONFIG_USE_SOUNDS)
        @Config.Comment("Use sounds")
        public boolean useSounds = true;


        @Config.LangKey(MPALIbConstants.CONFIG_DEBUGGING_INFO)
        @Config.Comment("Debugging info")
        public boolean isDebugging = false;

        @Config.LangKey(MPALIbConstants.CONFIG_MEK_J_TO_RF_RATIO)
        @Config.Comment("Mekanism Joules equals how many RF")
        public double mekRatio = 0.4;

        // 1 RF = 0.25 EU
        // 1 EU = 4 RF
        @Config.LangKey(MPALIbConstants.CONFIG_IC2_EU_TO_RF_RATIO)
        @Config.Comment("IndustrialCraft2 EU equals how many RF")
        @Config.RequiresWorldRestart
        public double ic2Ratio = 4D;


        @Config.LangKey(MPALIbConstants.CONFIG_RS_TO_RF_RATIO)
        @Config.Comment("Refined PlayerFOVStateStorage  energy equals how many RF")
        @Config.RequiresWorldRestart
        public double rsRatio = 1D;


        @Config.LangKey(MPALIbConstants.CONFIG_AE_TO_RF_RATIO)
        @Config.Comment("Applied Energistics AE energy equals how many RF")
        @Config.RequiresWorldRestart
        public double ae2Ratio = 2D;


        // (100KJ or 1M-RF)
        @Config.LangKey(MPALIbConstants.CONFIG_TIER_1_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 1.")
        @Config.RangeInt(min = 0)
        public int maxTier1 = (int) (1 * Math.pow(10, 6));

        // advanced capacitor (500KJ or 5M-RF)
        @Config.LangKey(MPALIbConstants.CONFIG_TIER_2_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 2.")
        @Config.RangeInt(min = 0)
        public int maxTier2 = (int) (5 * Math.pow(10, 6));

        // elite capacitor (5MJ or 50M-RF)
        @Config.LangKey(MPALIbConstants.CONFIG_TIER_3_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 3.")
        @Config.RangeInt(min = 0)
        public int maxTier3 = (int) (5 * Math.pow(10, 7));

        // ultimate capacitor (10MJ or 100M-RF)
        @Config.LangKey(MPALIbConstants.CONFIG_TIER_4_ENERGY_LVL)
        @Config.Comment("Maximum amount of RF energy for Tier 4.")
        @Config.RangeInt(min = 0)
        public int maxTier4 = (int) (1 * Math.pow(10, 8));
    }

    @Mod.EventBusSubscriber(modid = MPALIbConstants.MODID)
    public class ConfigSyncHandler {
        @SubscribeEvent
        public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(MPALIbConstants.MODID)) {
                ConfigManager.load(MPALIbConstants.MODID, Config.Type.INSTANCE);
                MPALibLogger.logger.info("Configuration has been saved.");
            }
        }
    }
}