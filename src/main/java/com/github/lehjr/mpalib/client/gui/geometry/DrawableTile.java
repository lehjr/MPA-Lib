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

import com.mojang.blaze3d.platform.GlStateManager;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.opengl.GL11;

public class DrawableTile extends RelativeRect {
    Colour backgroundColour;
    Colour borderColour;

    public DrawableTile(double left, double top, double right, double bottom, boolean growFromMiddle,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableTile(double left, double top, double right, double bottom,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, false);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableTile(Point2D ul, Point2D br,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(ul, br);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    @Override
    public DrawableTile copyOf() {
        return new DrawableTile(super.left(), super.top(), super.right(), super.bottom(),
                (this.ul != this.ulFinal || this.wh != this.whFinal), backgroundColour, borderColour);
    }

    @Override
    public DrawableTile setLeft(double value) {
        super.setLeft(value);
        return this;
    }

    @Override
    public DrawableTile setRight(double value) {
        super.setRight(value);
        return this;
    }

    @Override
    public DrawableTile setTop(double value) {
        super.setTop(value);
        return this;
    }

    @Override
    public DrawableTile setBottom(double value) {
        super.setBottom(value);
        return this;
    }

    @Override
    public DrawableTile setWidth(double value) {
        super.setWidth(value);
        return this;
    }

    @Override
    public DrawableTile setHeight(double value) {
        super.setHeight(value);
        return this;
    }

    void vertices() {
        GlStateManager.vertex3f((float)left(), (float)top(), 1);
        GlStateManager.vertex3f((float)right(), (float)top(), 1);
        GlStateManager.vertex3f((float)right(), (float)bottom(), 1);
        GlStateManager.vertex3f((float)left(), (float)bottom(), 1);
    }

    public void preDraw() {
        RenderState.on2D();
        RenderState.texturelessOn();

        // makes the lines and radii nicer
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT,  GL11.GL_NICEST);
    }

    public void postDraw() {
        Colour.WHITE.doGL();
        RenderState.texturelessOff();
        RenderState.glowOff();
    }

    public void draw() {
        float lineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        boolean smooth  = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);

        preDraw();

        GlStateManager.begin(GL11.GL_POLYGON);
        backgroundColour.doGL();
        vertices();
        GlStateManager.end();

        GlStateManager.begin(GL11.GL_LINE_LOOP);
//        GlStateManager.lineWidth(4f);
        borderColour.doGL();
        vertices();
        GlStateManager.end();
        postDraw();

        if (!smooth)
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);
    }
    public DrawableTile setBackgroundColour(Colour insideColour) {
        this.backgroundColour = insideColour;
        return this;
    }

    public DrawableTile setBorderColour(Colour outsideColour) {
        this.borderColour = outsideColour;
        return this;
    }
}