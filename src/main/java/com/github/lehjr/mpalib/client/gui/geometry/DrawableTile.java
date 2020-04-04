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

package com.github.lehjr.mpalib.client.gui.geometry;

import com.github.lehjr.mpalib.math.Colour;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class DrawableTile extends RelativeRect {
    Colour backgroundColour;
    Colour borderColour;
    float zLevel = 1;
    boolean shrinkBorder = false;

    public DrawableTile(float left, float top, float right, float bottom, boolean growFromMiddle,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableTile(float left, float top, float right, float bottom,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, false);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableTile(Point2F ul, Point2F br,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(ul, br);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    /**
     * determine if the border should be smaller than the background rectangle (like tooltips)
     * @param shrinkBorder
     */
    public void setShrinkBorder(boolean shrinkBorder) {
        this.shrinkBorder = shrinkBorder;
    }

    @Override
    public DrawableTile copyOf() {
        return new DrawableTile(super.left(), super.top(), super.right(), super.bottom(),
                (this.ul != this.ulFinal || this.wh != this.whFinal), backgroundColour, borderColour);
    }

    @Override
    public DrawableTile setLeft(float value) {
        super.setLeft(value);
        return this;
    }

    @Override
    public DrawableTile setRight(float value) {
        super.setRight(value);
        return this;
    }

    @Override
    public DrawableTile setTop(float value) {
        super.setTop(value);
        return this;
    }

    @Override
    public DrawableTile setBottom(float value) {
        super.setBottom(value);
        return this;
    }

    @Override
    public DrawableTile setWidth(float value) {
        super.setWidth(value);
        return this;
    }

    @Override
    public DrawableTile setHeight(float value) {
        super.setHeight(value);
        return this;
    }

    public DrawableTile setBackgroundColour(Colour insideColour) {
        this.backgroundColour = insideColour;
        return this;
    }

    public DrawableTile setBorderColour(Colour outsideColour) {
        this.borderColour = outsideColour;
        return this;
    }

    void draw(Colour colour, int glMode, float shrinkBy) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(glMode, DefaultVertexFormats.POSITION_COLOR);
        buffer.pos(right() - shrinkBy, top() + shrinkBy, zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(left() + shrinkBy, top() + shrinkBy, zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(left() + shrinkBy, bottom() - shrinkBy, zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(right() - shrinkBy, bottom() - shrinkBy, zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        tessellator.draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public void drawBackground() {
        draw(backgroundColour, GL11.GL_QUADS, 0);
    }

    public void drawBorder(float shrinkBy) {
        draw(borderColour, GL11.GL_LINE_LOOP, shrinkBy);
    }

    public void draw(float zLevel) {
        this.zLevel = zLevel;
        drawBackground();
        drawBorder(shrinkBorder ? 1 : 0);
    }
}