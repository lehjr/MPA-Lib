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

package com.github.lehjr.mpalib.util.client.gui.geometry;

import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
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

    public DrawableTile setBackgroundColour(Colour insideColour) {
        this.backgroundColour = insideColour;
        return this;
    }

    public DrawableTile setBorderColour(Colour outsideColour) {
        this.borderColour = outsideColour;
        return this;
    }

    void draw(MatrixStack matrix4f, Colour colour, int glMode, double shrinkBy) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(glMode, DefaultVertexFormats.POSITION_COLOR);

        buffer.pos(matrix4f.getLast().getMatrix(), (float)(right() - shrinkBy), (float) (top() + shrinkBy), zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(matrix4f.getLast().getMatrix(),(float)(left() + shrinkBy), (float) (top() + shrinkBy), zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(matrix4f.getLast().getMatrix(),(float)(left() + shrinkBy), (float) (bottom() - shrinkBy), zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        buffer.pos(matrix4f.getLast().getMatrix(),(float)(right() - shrinkBy), (float) (bottom() - shrinkBy), zLevel).color(colour.r, colour.g, colour.b, colour.a).endVertex();
        tessellator.draw();

        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public void drawBackground(MatrixStack matrixStack) {
        draw(matrixStack, backgroundColour, GL11.GL_QUADS, 0);
    }

    public void drawBorder(MatrixStack matrixStack, double shrinkBy) {
        draw(matrixStack, borderColour, GL11.GL_LINE_LOOP, shrinkBy);
    }

    public void draw(MatrixStack matrixStack, float zLevel) {
        this.zLevel = zLevel;
        drawBackground(matrixStack);
        drawBorder(matrixStack, shrinkBorder ? 1 : 0);
    }

    @Override
    public String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append(this.getClass()).append(":\n");
        stringbuilder.append("Center: ").append(center()).append("\n");
        stringbuilder.append("Left: ").append(left()).append("\n");
        stringbuilder.append("FinalLeft: ").append(finalLeft()).append("\n");
        stringbuilder.append("Right: ").append(right()).append("\n");
        stringbuilder.append("FinalRight: ").append(finalRight()).append("\n");
        stringbuilder.append("Bottom: ").append(bottom()).append("\n");
        stringbuilder.append("FinalBottom: ").append(finalBottom()).append("\n");
        stringbuilder.append("Top: ").append(top()).append("\n");
        stringbuilder.append("FinalTop: ").append(finalTop()).append("\n");
        stringbuilder.append("Width: ").append(left()).append("\n");
        stringbuilder.append("FinalWidthLeft: ").append(left()).append("\n");
        stringbuilder.append("Height: ").append(height()).append("\n");
        stringbuilder.append("FinalHeight: ").append(finalHeight()).append("\n");
        return stringbuilder.toString();
    }
}