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

package com.github.lehjr.mpalib.client.gui.hud.meters;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.client.render.IconUtils;
import com.github.lehjr.mpalib.client.render.TextureUtils;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.lwjgl.opengl.GL11;

public class HeatMeter {
    final int xsize = 32;
    final int ysize = 8;
    double meterStart, meterLevel, alpha;

    public double getAlpha() {
        return 0.7;
    }

    public Colour getColour() {
        return Colour.RED;
    }

    public TextureAtlasSprite getTexture() {
        return Minecraft.getInstance().getTextureMap().getAtlasSprite("minecraft:block/snow");
    }

    public void draw(double xpos, double ypos, double value) {
        TextureUtils.pushTexture(TextureUtils.TEXTURE_QUILT);
        RenderState.blendingOn();
        RenderState.on2D();
        drawFluid(xpos, ypos, value, getTexture());
        drawGlass(xpos, ypos);
        RenderState.off2D();
        RenderState.blendingOff();
        TextureUtils.popTexture();
    }

    public void drawFluid(double xpos, double ypos, double value, TextureAtlasSprite icon) {
        GL11.glPushMatrix();
        GL11.glScaled(0.5, 0.5, 0.5);

        // New: Horizontal, fill from left.
        meterStart = xpos;
        meterLevel = (xpos + xsize * value);

        while (meterStart + 8 < meterLevel) {
            IconUtils.drawIconAt(meterStart * 2, ypos * 2, icon, getColour().withAlpha(getAlpha()));
            meterStart += 8;
        }
        IconUtils.drawIconPartial(meterStart * 2, ypos * 2, icon, getColour().withAlpha(getAlpha()), 0, 0, (meterLevel - meterStart) * 2, 16);
        GL11.glPopMatrix();
    }

    public void drawGlass(double xpos, double ypos) {
        TextureUtils.pushTexture(MPALIbConstants.GLASS_TEXTURE);
        Colour.WHITE.doGL();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2d(0, 0);
        GL11.glVertex2d(xpos, ypos);
        GL11.glTexCoord2d(0, 1);
        GL11.glVertex2d(xpos + xsize, ypos);
        GL11.glTexCoord2d(1, 1);
        GL11.glVertex2d(xpos + xsize, ypos + ysize);
        GL11.glTexCoord2d(1, 0);
        GL11.glVertex2d(xpos, ypos + ysize);
        GL11.glEnd();
        TextureUtils.popTexture();
    }
}