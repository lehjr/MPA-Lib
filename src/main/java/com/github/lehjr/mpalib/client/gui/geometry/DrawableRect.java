/*
 * MPA-Lib (Formerly known as Numina)
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

import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class DrawableRect extends Rect {
    Colour backgroundColour;
    Colour borderColour;
    DoubleBuffer vertices;
    DoubleBuffer backgroundColours;
    DoubleBuffer borderColours;
    double cornerradius = 3;
    double zLevel = 1;

    public DrawableRect(double left, double top, double right, double bottom, boolean growFromMiddle,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableRect(double left, double top, double right, double bottom,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(left, top, right, bottom, false);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableRect(Point2D ul, Point2D br,
                        Colour backgroundColour,
                        Colour borderColour) {
        super(ul, br);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableRect(Rect ref, Colour backgroundColour, Colour borderColour) {
        super(ref.left(), ref.top(), ref.right(), ref.bottom(), ref.growFromMiddle());
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    @Override
    public DrawableRect copyOf() {
        return new DrawableRect(super.left(), super.top(), super.right(), super.bottom(),
                (this.ul != this.ulFinal || this.wh != this.whFinal) , backgroundColour, borderColour);
    }

    @Override
    public DrawableRect setLeft(double value) {
        super.setLeft(value);
        return this;
    }

    @Override
    public DrawableRect setRight(double value) {
        super.setRight(value);
        return this;
    }

    @Override
    public DrawableRect setTop(double value) {
        super.setTop(value);
        return this;
    }

    @Override
    public DrawableRect setBottom(double value) {
        super.setBottom(value);
        return this;
    }

    @Override
    public DrawableRect setWidth(double value) {
        super.setWidth(value);
        return this;
    }

    @Override
    public DrawableRect setHeight(double value) {
        super.setHeight(value);
        return this;
    }

    public void preDraw() {
        if (vertices == null || backgroundColours == null || borderColours == null
                || (this.ul != this.ulFinal || this.wh != this.whFinal)) {

            // top left corner
            DoubleBuffer corner = GradientAndArcCalculator.getArcPoints(Math.PI,
                    3.0 * Math.PI / 2.0, cornerradius, left() + cornerradius,
                    top() + cornerradius, zLevel);

            vertices = BufferUtils.createDoubleBuffer(corner.limit() * 4);
            vertices.put(corner);

            // bottom left corner
            corner = GradientAndArcCalculator.getArcPoints(3.0 * Math.PI / 2.0,
                    2.0 * Math.PI, cornerradius, left() + cornerradius,
                    bottom() - cornerradius, zLevel);
            vertices.put(corner);

            // bottom right corner
            corner = GradientAndArcCalculator.getArcPoints(0, Math.PI / 2.0, cornerradius,
                    right() - cornerradius, bottom() - cornerradius, zLevel);
            vertices.put(corner);


            // top right corner
            corner = GradientAndArcCalculator.getArcPoints(Math.PI / 2.0, Math.PI,
                    cornerradius, right() - cornerradius, top() + cornerradius,
                    zLevel);
            vertices.put(corner);
            vertices.flip();

            backgroundColours = GradientAndArcCalculator.getColourGradient(backgroundColour,
                    backgroundColour, vertices.limit() * 4 / 3 + 8);
            borderColours = GradientAndArcCalculator.getColourGradient(borderColour,
                    borderColour, vertices.limit() * 4 / 3 + 8);
        }

        RenderState.blendingOn();
        RenderState.on2D();
        RenderState.texturelessOn();
        RenderState.arraysOnColor();
    }

    public void drawBackground() {
        // render inside
        GL11.glColorPointer(4, 0, backgroundColours);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, vertices.limit() / 3);
    }

    public void drawBorder() {
        // render border
        GL11.glColorPointer(4, 0, borderColours);
        GL11.glVertexPointer(3, 0, vertices);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, vertices.limit() / 3);
    }

    public void postDraw() {
        RenderState.texturelessOff();
        RenderState.off2D();
        RenderState.blendingOff();
        RenderState.arraysOff();
    }

    public void draw() {
        preDraw();
        drawBackground();
        drawBorder();
        postDraw();
    }

    public DrawableRect setBackgroundColour(Colour backgroundColour) {
        this.backgroundColour = backgroundColour;
        return this;
    }

    public DrawableRect setBorderColour(Colour borderColour) {
        this.borderColour = borderColour;
        return this;
    }
}