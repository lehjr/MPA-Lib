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
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class DrawableRelativeRect extends RelativeRect {
    Colour backgroundColour;
    Colour borderColour;
    float cornerradius = 3;
    float zLevel = 1;
    boolean shrinkBorder = true;

    public DrawableRelativeRect(float left, float top, float right, float bottom, boolean growFromMiddle,
                                Colour backgroundColour,
                                Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableRelativeRect(float left, float top, float right, float bottom,
                                Colour backgroundColour,
                                Colour borderColour) {
        super(left, top, right, bottom, false);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableRelativeRect(Point2F ul, Point2F br,
                                Colour backgroundColour,
                                Colour borderColour) {
        super(ul, br);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    @Override
    public DrawableRelativeRect copyOf() {
        return new DrawableRelativeRect(super.left(), super.top(), super.right(), super.bottom(),
                (this.ul != this.ulFinal || this.wh != this.whFinal) , backgroundColour, borderColour);
    }

    @Override
    public DrawableRelativeRect setLeft(float value) {
        super.setLeft(value);
        return this;
    }

    @Override
    public DrawableRelativeRect setRight(float value) {
        super.setRight(value);
        return this;
    }

    @Override
    public DrawableRelativeRect setTop(float value) {
        super.setTop(value);
        return this;
    }

    @Override
    public DrawableRelativeRect setBottom(float value) {
        super.setBottom(value);
        return this;
    }

    @Override
    public DrawableRelativeRect setWidth(float value) {
        super.setWidth(value);
        return this;
    }

    @Override
    public DrawableRelativeRect setHeight(float value) {
        super.setHeight(value);
        return this;
    }

    public DrawableRelativeRect setBackgroundColour(Colour backgroundColour) {
        this.backgroundColour = backgroundColour;
        return this;
    }

    public DrawableRelativeRect setBorderColour(Colour borderColour) {
        this.borderColour = borderColour;
        return this;
    }

    public FloatBuffer preDraw(float shrinkBy) {
        FloatBuffer vertices;
        // top left corner
        FloatBuffer corner = GradientAndArcCalculator.getArcPoints(
                (float) Math.PI,
                (float)(3.0 * Math.PI / 2.0),
                cornerradius,
                left() + shrinkBy + cornerradius,
                top() + shrinkBy + cornerradius);

        vertices = BufferUtils.createFloatBuffer(corner.limit() * 4);
        vertices.put(corner);

        // bottom left corner
        corner = GradientAndArcCalculator.getArcPoints(
                (float)(3.0 * Math.PI / 2.0F),
                (float)(2.0 * Math.PI),
                cornerradius,
                left() + shrinkBy + cornerradius,
                bottom() - shrinkBy - cornerradius);
        vertices.put(corner);

        // bottom right corner
        corner = GradientAndArcCalculator.getArcPoints(
                0,
                (float) (Math.PI / 2.0),
                cornerradius,
                right() - shrinkBy - cornerradius,
                bottom() - shrinkBy - cornerradius);
        vertices.put(corner);

        // top right corner
        corner = GradientAndArcCalculator.getArcPoints(
                (float) (Math.PI / 2.0),
                (float) Math.PI,
                cornerradius,
                right() - shrinkBy - cornerradius,
                top() + shrinkBy + cornerradius);
        vertices.put(corner);
        vertices.flip();

        return vertices;
    }

    public void drawBackground(FloatBuffer vertices) {
        drawBuffer(vertices, backgroundColour, GL11.GL_TRIANGLE_FAN);
    }

    public void drawBorder(FloatBuffer vertices) {
        drawBuffer(vertices, borderColour, GL11.GL_LINE_LOOP);
    }

    void drawBuffer(FloatBuffer vertices, Colour colour, int glMode) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(glMode, DefaultVertexFormats.POSITION_COLOR);

        while (vertices.hasRemaining()) {
            buffer.pos(vertices.get(), vertices.get(), zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        }
        tessellator.draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public void draw(float zLevel) {
        this.zLevel = zLevel;
        FloatBuffer vertices = preDraw(0);
        drawBackground(vertices);

        if (shrinkBorder) {
            vertices = preDraw(1);
        } else {
            vertices.rewind();
        }
        drawBorder(vertices);
    }
}