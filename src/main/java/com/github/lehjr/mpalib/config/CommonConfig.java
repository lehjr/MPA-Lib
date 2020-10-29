package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
        public ForgeConfigSpec.IntValue ARMOR_STAND_MAX_POWER;

        public CommonConfig(ForgeConfigSpec.Builder builder) {
                builder.comment("General settings").push("General");
                {
                        ARMOR_STAND_MAX_POWER = builder
                                .comment("Ignore speed boosts for field of view")
                                .translation(MPALibConstants.CONFIG_USE_FOV_FIX)
                                .defineInRange("armorStandMaxPower", 1000000, 100, 10000000);
                }
                builder.pop();

                builder.push("Modules");
                builder.push("Energy Storage");
                {
                        builder.push("battery_basic");
                        builder.defineInRange("base_maxEnergy", 1000000.0D, 0, 1.7976931348623157E308);
                        builder.define("isAllowed", true);
                        builder.defineInRange("base_maxTransfer", 1000000.0D, 0, 1.7976931348623157E308);
                        builder.pop();
                }
                {
                        builder.push("battery_advanced");
                        builder.defineInRange("base_maxEnergy", 5000000.0D, 0, 1.7976931348623157E308);
                        builder.define("isAllowed", true);
                        builder.defineInRange("base_maxTransfer", 5000000.0D, 0, 1.7976931348623157E308);
                        builder.pop();
                }
                {
                        builder.push("battery_elite");
                        builder.defineInRange("base_maxEnergy", 5.0E7D, 0, 1.7976931348623157E308);
                        builder.define("isAllowed", true);
                        builder.defineInRange("base_maxTransfer", 5.0E7D, 0, 1.7976931348623157E308);
                        builder.pop();
                }
                {
                        builder.push("battery_ultimate");
                        builder.defineInRange("base_maxEnergy", 1.0E8D, 0, 1.7976931348623157E308);
                        builder.define("isAllowed", true);
                        builder.defineInRange("base_maxTransfer", 1.0E8D, 0, 1.7976931348623157E308);
                        builder.pop();
                }
                builder.pop();
                builder.pop();
        }
}