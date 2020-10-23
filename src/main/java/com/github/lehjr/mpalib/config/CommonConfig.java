package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
        public ForgeConfigSpec.IntValue ARMOR_STAND_MAX_POWER;








        public CommonConfig(ForgeConfigSpec.Builder builder) {
                builder.comment("General settings").push("General");
                ARMOR_STAND_MAX_POWER = builder
                                        .comment("Ignore speed boosts for field of view")
                                        .translation(MPALibConstants.CONFIG_USE_FOV_FIX)
                                        .defineInRange("armorStandMaxPower", 1000000, 100, 10000000);
                builder.pop();
                builder.push("Modules");

                builder.pop();
        }
}