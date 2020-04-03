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

import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:37 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
@OnlyIn(Dist.CLIENT)
public class IconUtils {
    static TextureAtlasSprite getMissingIcon() {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(MissingTextureSprite.getLocation());
    }

    /**
     * Draws a MuseIcon
     *
     * @param x
     * @param y
     * @param icon
     * @param colour
     */
    public static void drawIconAt(float x, float y, TextureAtlasSprite icon, Colour colour) {
        drawIconPartial(x, y, icon, colour, 0, 0, 16, 16);
    }

    public static void drawIconPartialOccluded(float x, float y, TextureAtlasSprite icon, Colour colour, float left, float top, float right, float bottom) {
        float xmin = MathUtils.clampFloat(left - x, 0, 16);
        float ymin = MathUtils.clampFloat(top - y, 0, 16);
        float xmax = MathUtils.clampFloat(right - x, 0, 16);
        float ymax = MathUtils.clampFloat(bottom - y, 0, 16);
        drawIconPartial(x, y, icon, colour, xmin, ymin, xmax, ymax);
    }

    /**
     * Draws a MuseIcon
     *
     * @param x
     * @param y
     * @param icon
     * @param colour
     */
    public static void drawIconPartial(double x, double y, TextureAtlasSprite icon, Colour colour, float left, float top, float right, float bottom) {
        if (icon == null) {
            icon = getMissingIcon();
        }

        GL11.glPushMatrix();
        RenderState.on2D();
        RenderState.blendingOn();
        if (colour != null) {
            colour.doGL();
        }

        Tessellator tess = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tess.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

        float u1 = icon.getMinU();
        float v1 = icon.getMinV();
        float u2 = icon.getMaxU();
        float v2 = icon.getMaxV();

        float xoffset1 = left * (u2 - u1) / 16.0f;
        float yoffset1 = top * (v2 - v1) / 16.0f;
        float xoffset2 = right * (u2 - u1) / 16.0f;
        float yoffset2 = bottom * (v2 - v1) / 16.0f;

        bufferBuilder.pos(x + left, y + top, 0);
        bufferBuilder.tex(u1 + xoffset1, v1 + yoffset1);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + left, y + bottom, 0);
        bufferBuilder.tex(u1 + xoffset1, v1 + yoffset2);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + right, y + bottom, 0);
        bufferBuilder.tex(u1 + xoffset2, v1 + yoffset2);
        bufferBuilder.endVertex();

        bufferBuilder.pos(x + right, y + top, 0);
        bufferBuilder.tex(u1 + xoffset2, v1 + yoffset1);
        bufferBuilder.endVertex();

        tess.draw();
        RenderState.blendingOff();
        RenderState.off2D();
        GL11.glPopMatrix();
    }
}