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

package com.github.lehjr.mpalib.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;

import java.util.Stack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:38 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
public final class MuseTextureUtils {
    private static final Stack<ResourceLocation> texturestack = new Stack<>();
    private static ResourceLocation TEXTURE_MAP = AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    public static final ResourceLocation TEXTURE_QUILT = AtlasTexture.LOCATION_BLOCKS_TEXTURE;

    static {
        new MuseTextureUtils();
    }

    private MuseTextureUtils() {
    }

    public static void pushTexture(final ResourceLocation locationIn) {
        texturestack.push(TEXTURE_MAP);
        TEXTURE_MAP = locationIn;
        bindTexture(TEXTURE_MAP);
    }

    public static void popTexture() {
        TEXTURE_MAP = texturestack.pop();
        bindTexture(TEXTURE_MAP);
    }

    public static void bindTexture(ResourceLocation locationIn) {
        Minecraft.getInstance().getTextureManager().bindTexture(locationIn);
    }
}