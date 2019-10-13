/*
 * Copyright (c) 2019 leon
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

import net.minecraft.util.ResourceLocation;

public class MPALIbConstants {
    // Mod
    public static final String MODID = "mpalib";

    // Misc
    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String TEXTURE_PREFIX = RESOURCE_PREFIX + "textures/";
    public static final ResourceLocation LIGHTNING_TEXTURE = new ResourceLocation(TEXTURE_PREFIX + "gui/lightning-medium.png");
    public static final ResourceLocation GLASS_TEXTURE = new ResourceLocation(TEXTURE_PREFIX + "gui/glass.png");
    public static final String BLANK_ARMOR_MODEL_PATH = TEXTURE_PREFIX + "item/armor/blankarmor.png";

    // NuminaConfig
    public static final String CONFIG_PREFIX = "config." + MODID + ".";
    public static final String CONFIG_USE_FOV_FIX = CONFIG_PREFIX + "useFOVFix";
    public static final String CONFIG_USE_FOV_NORMALIZE = CONFIG_PREFIX + "normalizeFOV";
    public static final String CONFIG_FOV_FIX_DEAULT_STATE = CONFIG_PREFIX + "FOVFixDefaultState";
    public static final String CONFIG_USE_SOUNDS = CONFIG_PREFIX + "useSounds";
    public static final String CONFIG_DEBUGGING_INFO = CONFIG_PREFIX + "useDebuggingInfo";

    public static final String CONFIG_TIER_1_ENERGY_LVL = CONFIG_PREFIX + "tier1EnergyLevel";
    public static final String CONFIG_TIER_2_ENERGY_LVL = CONFIG_PREFIX + "tier2EnergyLevel";
    public static final String CONFIG_TIER_3_ENERGY_LVL = CONFIG_PREFIX + "tier3EnergyLevel";
    public static final String CONFIG_TIER_4_ENERGY_LVL = CONFIG_PREFIX + "tier4EnergyLevel";

    // String for overheat damage
    public static final String OVERHEAT_DAMAGE = "Overheat";

    // ModelSpec
    public static final String NBT_TEXTURESPEC_TAG = "texSpec";
    public static final String NBT_SPECLIST_TAG = "specList";
    public static final String TAG_RENDER = "render";
    public static final String TAG_COSMETIC_PRESET = "cosmeticPreset";
    public static final String TAG_COLOURS = "colours";
    public static final String TAG_MODEL = "model";
    public static final String TAG_PART = "part";
    public static final String TAG_GLOW = "glow";
    public static final String TAG_COLOUR_INDEX = "colourindex";

    public static final String MODULE_TRADEOFF_PREFIX = "module.tradeoff.";

}