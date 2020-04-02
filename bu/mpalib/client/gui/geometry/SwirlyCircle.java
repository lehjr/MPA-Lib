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

import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;


public class SwirlyCircle {
    public static final double detail = 2;
    protected DoubleBuffer points;
    protected DoubleBuffer colour;
    int numsegments;

    public SwirlyCircle(Colour c1, Colour c2) {
        if (points == null) {
            points = GradientAndArcCalculator.getArcPoints(0, Math.PI * 2 + 0.0001, detail, 0, 0, 0);
        }
        numsegments = points.limit() / 3;
        colour = GradientAndArcCalculator.getColourGradient(c1, c2, points.limit() / 3);
    }

    public void draw(double radius, double x, double y) {
        int length = points.limit();
        double ratio = (System.currentTimeMillis() % 2000) / 2000.0;
        colour.rewind();
        points.rewind();
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 0);
        GL11.glScaled(radius / detail, radius / detail, 1.0);
        GL11.glRotatef((float) (-ratio * 360.0), 0, 0, 1);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        RenderState.arraysOnColor();
        RenderState.texturelessOn();
        RenderState.blendingOn();
        RenderState.glColorPointer(4, 0, colour);
        RenderState.glVertexPointer(3, 0, points);
        GL11.glDrawArrays(GL11.GL_LINE_LOOP, 0, length / 3);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        RenderState.blendingOff();
        RenderState.texturelessOff();
        RenderState.arraysOff();
        GL11.glPopMatrix();
    }
}