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

package com.github.lehjr.mpalib.basemod;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MPALibConfig {
    public static final ClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final CommonConfig COMMON_CONFIG;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT_SPEC = clientSpecPair.getRight();
            CLIENT_CONFIG = clientSpecPair.getLeft();
        }
        {
            final Pair<CommonConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
            COMMON_SPEC = serverSpecPair.getRight();
            COMMON_CONFIG = serverSpecPair.getLeft();
        }
    }

    static File setupConfigFile(String fileName) {
        Path configFile = Paths.get("config/lehjr").resolve(MPALIbConstants.MODID).resolve(fileName);
        File cfgFile = configFile.toFile();
        try {
            if (!cfgFile.getParentFile().exists())
                cfgFile.getParentFile().mkdirs();
            if (!cfgFile.exists())
                cfgFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfgFile;
    }

    public static ForgeConfigSpec.BooleanValue
            USE_FOV_FIX,
            USE_FOV_NORMALIZE,
            FOV_FIX_DEAULT_STATE,
            USE_SOUNDS,
            DEBUGGING_INFO;

    public static class ClientConfig {
        ClientConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("General settings").push("General");

            USE_FOV_FIX = builder
                    .comment("Ignore speed boosts for field of view")
                    .translation(MPALIbConstants.CONFIG_USE_FOV_FIX)
                    .define("useFOVFix", true);

            USE_FOV_NORMALIZE = builder
                    .comment("Use FOV Fix to normalize FOV changes")
                    .translation(MPALIbConstants.CONFIG_USE_FOV_NORMALIZE)
                    .define("useFOVNormalize", true);


            FOV_FIX_DEAULT_STATE = builder
                    .comment("Default state of FOVfix on login (enabled = true, disabled = false)")
                    .translation(MPALIbConstants.CONFIG_FOV_FIX_DEAULT_STATE)
                    .define("fovFixDefaultState", true);

            USE_SOUNDS = builder
                    .comment("Use sounds")
                    .translation(MPALIbConstants.CONFIG_USE_SOUNDS)
                    .define("useSounds", true);

            DEBUGGING_INFO = builder
                    .comment("Debugging info")
                    .translation(MPALIbConstants.CONFIG_DEBUGGING_INFO)
                    .define("useSounds", true);

            builder.pop().comment("Tiers per RF/FE").push("Tiers");
        }
    }

    public static final double MEK_J_TO_RF_RATIO = 0.4;

    // 1 RF = 0.25 EU
    // 1 EU = 4 RF
    public static final double IC2_EU_TO_RF_RATIO = 4D;
    public static final double RS_TO_RF_RATIO = 1D;
    public static final double AE_TO_RF_RATIO = 2D;

    public static class CommonConfig {
        public static ForgeConfigSpec.IntValue
                TIER_1_ENERGY_LVL,
                TIER_2_ENERGY_LVL,
                TIER_3_ENERGY_LVL,
                TIER_4_ENERGY_LVL;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
            builder.comment("Tiers per RF/FE").push("Tiers");
            // (100KJ or 1M-RF)
            TIER_1_ENERGY_LVL = builder
                    .comment("Maximum amount of RF energy for Tier 1.")
                    .translation(MPALIbConstants.CONFIG_TIER_1_ENERGY_LVL)
                    .defineInRange("maxTier1", ((int) (1 * Math.pow(10, 6))), 0, Integer.MAX_VALUE/4);

            // advanced capacitor (500KJ or 5M-RF)
            TIER_2_ENERGY_LVL = builder
                    .comment("Maximum amount of RF energy for Tier 2.")
                    .translation(MPALIbConstants.CONFIG_TIER_2_ENERGY_LVL)
                    .defineInRange("maxTier2", (int) (5 * Math.pow(10, 6)), 0, Integer.MAX_VALUE/3);


            // elite capacitor (5MJ or 50M-RF)
            TIER_3_ENERGY_LVL = builder
                    .comment("Maximum amount of RF energy for Tier 3.")
                    .translation(MPALIbConstants.CONFIG_TIER_3_ENERGY_LVL)
                    .defineInRange("maxTier3", (int) (5 * Math.pow(10, 7)), 0, Integer.MAX_VALUE/2);
            //        // elite capacitor (5MJ or 50M-RF)

            // ultimate capacitor (10MJ or 100M-RF)
            TIER_4_ENERGY_LVL = builder
                    .comment("Maximum amount of RF energy for Tier 4.")
                    .translation(MPALIbConstants.CONFIG_TIER_4_ENERGY_LVL)
                    .defineInRange("maxTier4", (int) (1 * Math.pow(10, 8)), 0, Integer.MAX_VALUE);
            builder.pop();
        }
    }
}