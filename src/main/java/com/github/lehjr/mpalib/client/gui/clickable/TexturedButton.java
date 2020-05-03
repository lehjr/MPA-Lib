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

package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.GuiIcon;
import com.github.lehjr.mpalib.client.gui.geometry.Point2F;
import com.github.lehjr.mpalib.client.gui.geometry.Rect;
import com.github.lehjr.mpalib.math.Colour;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TexturedButton extends Button {
    ResourceLocation textureLocation;
    Float textureX;
    Float textureY;
    float textureWidth;
    float textureHeight;

    public TexturedButton(float left, float top, float right, float bottom, boolean growFromMiddle,
                          Colour backgroundColourEnabled,
                          Colour backgroundColourDisabled,
                          Colour borderColourEnabled,
                          Colour borderColourDisabled,
                          float textureWidth,
                          float textureHeight,
                          ResourceLocation textureLocation) {
        super(left, top, right, bottom, growFromMiddle, backgroundColourEnabled, backgroundColourDisabled, borderColourEnabled, borderColourDisabled);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureLocation = textureLocation;
    }

    public TexturedButton(float left, float top, float right, float bottom,
                          Colour backgroundColourEnabled,
                          Colour backgroundColourDisabled,
                          Colour borderColourEnabled,
                          Colour borderColourDisabled,
                          float textureWidth,
                          float textureHeight,
                          ResourceLocation textureLocation) {
        super(left, top, right, bottom, backgroundColourEnabled, backgroundColourDisabled, borderColourEnabled, borderColourDisabled);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureLocation = textureLocation;
    }

    public TexturedButton(Point2F ul, Point2F br,
                          Colour backgroundColourEnabled,
                          Colour backgroundColourDisabled,
                          Colour borderColourEnabled,
                          Colour borderColourDisabled,
                          float textureWidth,
                          float textureHeight,
                          ResourceLocation textureLocation) {
        super(ul, br, backgroundColourEnabled, backgroundColourDisabled, borderColourEnabled, borderColourDisabled);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureLocation = textureLocation;
    }

    public TexturedButton(Rect ref,
                          Colour backgroundColourEnabled,
                          Colour backgroundColourDisabled,
                          Colour borderColourEnabled,
                          Colour borderColourDisabled,
                          float textureWidth,
                          float textureHeight,
                          ResourceLocation textureLocation) {
        super(ref, backgroundColourEnabled, backgroundColourDisabled, borderColourEnabled, borderColourDisabled);
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.textureLocation = textureLocation;
    }

    public void setTextureOffsetX(float xOffset) {
        textureX = xOffset;
    }

    public void setTextureOffsetY(float yOffset) {
        textureY = yOffset;
    }


    @Override
    public void render(int mouseX, int mouseY, float partialTicks, float zLevel) {
        super.render(mouseX, mouseY, partialTicks, zLevel);
        Colour color;
        if(this.isVisible && this.isEnabled) {
            color = this.hitBox(mouseX, mouseY) ? Colour.LIGHTBLUE.withAlpha(0.6F) : Colour.WHITE;
        } else {
            color = Colour.RED.withAlpha(0.6F);
        }

        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(this.textureLocation);
        RenderSystem.disableDepthTest();

        GuiIcon.blit(
                finalLeft(),
                finalRight(),
                finalTop(),
                finalBottom(),
                zLevel,
                finalWidth(),
                finalHeight(),
                textureX == null ? 0 : textureX,
                textureY == null ? 0 : textureY,
                textureWidth,
                textureHeight,
                color);
        RenderSystem.enableDepthTest();
    }
}
