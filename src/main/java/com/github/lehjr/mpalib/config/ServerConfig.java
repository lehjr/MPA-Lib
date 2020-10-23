package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
        public ForgeConfigSpec.IntValue ARMOR_STAND_MAX_POWER;
        protected ForgeConfigSpec.BooleanValue RECIPES_USE_VANILLA;

        public ServerConfig(ForgeConfigSpec.Builder builder) {
                builder.comment("General settings").push("General");
                /** Recipes ----------------------------------------------------------------------------------------------- */
                builder.comment("Recipe settings").push("Recipes");
                RECIPES_USE_VANILLA = builder
                        .comment("Use recipes for Vanilla")
                        .translation(MPALibConstants.CONFIG_RECIPES_USE_VANILLA)
                        .worldRestart()
                        .define("useVanillaRecipes", true);
                builder.pop();


                ARMOR_STAND_MAX_POWER = builder
                                        .comment("Ignore speed boosts for field of view")
                                        .translation(MPALibConstants.CONFIG_USE_FOV_FIX)
                                        .defineInRange("armorStandMaxPower", 1000000, 100, 10000000);
                builder.pop();
                builder.push("Modules");

                builder.pop();
        }
}