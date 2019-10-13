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

package com.github.lehjr.mpalib.client.gui.geometry;

import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

public class DrawableMuseCircle {
    public static final double detail = 4;
    protected static DoubleBuffer points;
    protected final DoubleBuffer colour;

    public DrawableMuseCircle(Colour c1, Colour c2) {
        if (points == null) {
            DoubleBuffer arcPoints = GradientAndArcCalculator.getArcPoints(0, Math.PI * 2 + 0.0001, detail, 0, 0, 0);
            points = BufferUtils.createDoubleBuffer(arcPoints.limit() + 6);
            points.put(new double[]{0, 0, 0});
            points.put(arcPoints);
            arcPoints.rewind();
            points.put(arcPoints.get());
            points.put(arcPoints.get());
            points.put(arcPoints.get());
            points.flip();
        }
        DoubleBuffer colourPoints = GradientAndArcCalculator.getColourGradient(c1, c1, points.limit() / 3);
        colour = BufferUtils.createDoubleBuffer(colourPoints.limit() + 4);
        colour.put(c2.asArray());
        colour.put(colourPoints);
        colour.flip();
    }

    public void draw(double radius, double x, double y) {
        points.rewind();
        colour.rewind();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(radius / detail, radius / detail, 1.0);
        RenderState.on2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        RenderState.arraysOnColor();
        RenderState.texturelessOn();
        RenderState.blendingOn();
        RenderState.glColorPointer(4, 0, colour);
        RenderState.glVertexPointer(3, 0, points);
        GL11.glDrawArrays(GL11.GL_TRIANGLE_FAN, 0, points.limit() / 3);
        RenderState.blendingOff();
        RenderState.texturelessOff();
        RenderState.arraysOff();
        RenderState.off2D();
        GL11.glPopMatrix();
    }
}
