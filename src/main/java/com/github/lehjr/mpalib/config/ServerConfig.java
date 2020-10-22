package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
        public ForgeConfigSpec.IntValue ARMOR_STAND_MAX_POWER;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
                builder.comment("General settings").push("General");
                ARMOR_STAND_MAX_POWER = builder
                                        .comment("Ignore speed boosts for field of view")
                                        .translation(MPALIbConstants.CONFIG_USE_FOV_FIX)
                                        .defineInRange("armorStandMaxPower", 1000000, 100, 10000000);
                builder.pop();
                builder.push("Modules");

                builder.pop();
        }
}