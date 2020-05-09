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
import net.minecraft.util.ResourceLocation;

public class TexturedButton extends Button {
    ResourceLocation textureLocation;
    float texStartX;
    float texStartY;
    float textureWidth;
    float textureHeight;
    float iconWidth;
    float iconHeight;

    public TexturedButton(float left, float top, float right, float bottom, boolean growFromMiddle,
                          Colour backgroundColourEnabled,
                          Colour backgroundColourDisabled,
                          Colour borderColourEnabled,
                          Colour borderColourDisabled,
                          float textureWidth,
                          float textureHeight,
                          ResourceLocation textureLocation) {
        super(left, top, right, bottom, growFromMiddle, backgroundColourEnabled, backgroundColourDisabled, borderColourEnabled, borderColourDisabled);
        this.textureWidth = this.iconWidth = textureWidth;
        this.textureHeight = this.iconHeight = textureHeight;
        this.textureLocation = textureLocation;
        this.texStartX = 0;
        this.texStartY = 0;
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
        this.texStartX = 0;
        this.texStartY = 0;
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
        this.texStartX = 0;
        this.texStartY = 0;
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
        this.texStartX = 0;
        this.texStartY = 0;
    }

    public TexturedButton setTextureStartX(float xOffset) {
        this.texStartX = xOffset;
        return this;
    }

    public TexturedButton setTextureStartY(float yOffset) {
        this.texStartY = yOffset;
        return this;
    }

    public TexturedButton setIconWidth(float iconWidth) {
        this.iconWidth = iconWidth;
        return this;
    }

    public TexturedButton setIconHeight(float iconHeight) {
        this.iconHeight = iconHeight;
        return this;
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

        GuiIcon.renderTextureWithColour(this.textureLocation, left(), right(), top(), bottom(), zLevel,
        iconWidth, iconHeight, texStartX, texStartY, textureWidth, textureHeight, color);

    }
}
