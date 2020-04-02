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
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

public class DrawableArrow extends RelativeRect {
    Colour backgroundColour;
    Colour borderColour;
    boolean drawShaft = true;
    ArrowDirection facing = ArrowDirection.RIGHT;

    public DrawableArrow(double left, double top, double right, double bottom, boolean growFromMiddle,
                         Colour backgroundColour,
                         Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableArrow(double left, double top, double right, double bottom,
                         Colour backgroundColour,
                         Colour borderColour) {
        super(left, top, right, bottom, false);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableArrow(Point2D ul, Point2D br,
                         Colour backgroundColour,
                         Colour borderColour) {
        super(ul, br);
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public DrawableArrow(RelativeRect ref,
                         Colour backgroundColour,
                         Colour borderColour) {
        super(ref.left(), ref.top(), ref.right(), ref.bottom(), ref.growFromMiddle());
        this.backgroundColour = backgroundColour;
        this.borderColour = borderColour;
    }

    public void setBackgroundColour(Colour colour) {
        this.backgroundColour = colour;
    }

    public void setDirection(ArrowDirection facing) {
        this.facing = facing;
    }

    public void setDrawShaft(boolean drawShaft) {
        this.drawShaft = drawShaft;
    }

    void drawArrowShaftpt1() {
        switch (this.facing) {
            case RIGHT:
                GlStateManager.vertex3f((float)left(), (float) (centery() - (height()* 0.15)), 1);
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.15)), (float) (centery() - (height()* 0.15)), 1);
                break;
            case DOWN:
                GlStateManager.vertex3f((float) (centerx() + width()* 0.15), (float) top(), 1);
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.15)), (float) (centery() + (height()* 0.15)), 1);
                break;

            case LEFT:
                GlStateManager.vertex3f((float) right(), (float) (centery() + (height()* 0.15)), 1);
                GlStateManager.vertex3f((float) (centerx() - (width() * 0.15)), (float) (centery() + (height()* 0.15)), 1);
                break;

            case UP:
                GlStateManager.vertex3f((float) (centerx() - width()* 0.15), (float) bottom(), 1);
                GlStateManager.vertex3f((float) (centerx() - (width() * 0.15)), (float) (centery() - (height()* 0.15)), 1);
                break;
        }
    }

    void drawArrowHead() {
        switch (this.facing) {
            case RIGHT:
                GlStateManager.vertex3f(drawShaft ? (float) (centerx() + (width() * 0.15)) : (float)left(), (float) (centery() - (height() * 0.4F)), 1);
                GlStateManager.vertex3f((float)right(), (float)centery(), 1);
                GlStateManager.vertex3f(drawShaft ? (float) (centerx() + (width() * 0.15)) : (float)left(), (float) (centery() + (height() * 0.4F)), 1);
                break;
            case DOWN:
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.4F)),
                        drawShaft ? (float)top() : (float) (centery() + (height() * 0.15)),
                        1);
                GlStateManager.vertex3f((float)centerx(), (float)bottom(), 1);
                GlStateManager.vertex3f((float) (centerx() - (width() * 0.4F)),
                        drawShaft ? (float)top() : (float) (centery() + (height() * 0.15)),
                        1);
                break;
            case LEFT:
                GlStateManager.vertex3f(drawShaft ? (float) (centerx() - (width() * 0.15)) : (float) right(), (float) (centery() + (height() * 0.4F)), 1);
                GlStateManager.vertex3f((float)left(), (float)centery(), 1);
                GlStateManager.vertex3f(drawShaft ? (float) (centerx() - (width() * 0.15)) : (float) right(), (float) (centery() - (height() * 0.4F)), 1);
                break;
            case UP:
                GlStateManager.vertex3f((float) (centerx() - (width() * 0.4F)),
                        drawShaft ? (float)bottom() : (float) (centery() - (height() * 0.15)),
                        1);
                GlStateManager.vertex3f((float)centerx(), (float)top(), 1);
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.4F)),
                        drawShaft ? (float)bottom() : (float) (centery() - (height() * 0.15)),
                        1);

                break;
        }
    }

    void drawArrowShaftpt2() {
        switch (this.facing) {
            case RIGHT:
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.15)), (float) (centery() + (height() * 0.15)), 1);
                GlStateManager.vertex3f((float)left(), (float) (centery() + (height()* 0.15)), 1);
                break;
            case DOWN:
                GlStateManager.vertex3f((float) (centerx() - width()* 0.15), (float) (centery() - (height()* 0.15)), 1);
                GlStateManager.vertex3f((float) (centerx() - width()* 0.15), (float) top(), 1);
                break;
            case LEFT:
                GlStateManager.vertex3f((float) (centerx() - (width() * 0.15)), (float) (centery() - (height()* 0.15)), 1);
                GlStateManager.vertex3f((float) right(), (float) (centery() - (height()* 0.15)), 1);
                break;
            case UP:
                GlStateManager.vertex3f((float) (centerx() + width()* 0.15), (float) (centery() - (height()* 0.15)), 1);
                GlStateManager.vertex3f((float) (centerx() + (width() * 0.15)), (float) bottom(), 1);
                break;
        }
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

    public void postRender(int mouseX, int mouseY, float partialTicks) {

    }

    public void draw() {
        preDraw();

//        // draw arrow head
        GlStateManager.begin(GL11.GL_POLYGON);
        backgroundColour.doGL();
        drawArrowHead();
        GlStateManager.end();

        if (drawShaft) {
            // draw arrow shaft
            GlStateManager.begin(GL11.GL_POLYGON);
            backgroundColour.doGL();
            drawArrowShaftpt1();
            drawArrowShaftpt2();
            GlStateManager.end();
        }

        // draw arrow border
        GlStateManager.begin(GL11.GL_LINE_LOOP);
        borderColour.doGL();
        if (drawShaft)
            drawArrowShaftpt1();
        drawArrowHead();
        if (drawShaft)
            drawArrowShaftpt2();
        GlStateManager.end();
        postDraw();
    }

    public enum ArrowDirection {
        UP(270),
        DOWN(90),
        LEFT(180),
        RIGHT(0);

        int rotation;

        ArrowDirection(int rotation) {
            this.rotation = rotation;
        }

        public int getRotation() {
            return this.rotation;
        }
    }
}