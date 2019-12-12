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

package com.github.lehjr.mpalib.client.gui.scrollable;

import com.github.lehjr.mpalib.client.gui.frame.IGuiFrame;
import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected final int buttonsize = 5;
    protected int totalsize;
    protected int currentscrollpixels;
    protected int lastdWheel = Mouse.getDWheel();
    protected boolean visibile = true;
    protected boolean enabled = true;

    protected DrawableRect border;

    public ScrollableFrame(Point2D topleft, Point2D bottomright, Colour backgroundColour, Colour borderColour) {
        border = new DrawableRect(topleft, bottomright, backgroundColour, borderColour);
    }

    protected double getScrollAmount() {
        return 8;
    }

    public void setTopLeft(Point2D topLeft) {
        this.border.setTop(topLeft.getY());
        this.border.setLeft(topLeft.getX());
    }

    public void setBottomRight(Point2D bottomRight) {
        this.border.setBottom(bottomRight.getY());
        this.border.setRight(bottomRight.getX());
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        return this.border.containsPoint(mouseX, mouseY);
    }

    @Override
    public boolean onMouseUp(double mouseX, double mouseY, int button) {
        return  this.border.containsPoint(mouseX, mouseY);
    }

    @Override
    public void init(double left, double top, double right, double bottom) {
        this.border.setTargetDimensions(left, top, right, bottom);
    }

    @Override
    public void update(double x, double y) {
        if (border.containsPoint(x, y)) {
            int dscroll = (lastdWheel - Mouse.getDWheel()) / 15;
            lastdWheel = Mouse.getDWheel();
            if (Mouse.isButtonDown(0)) {
                if ((y - border.top()) < buttonsize && currentscrollpixels > 0) {
                    dscroll -= getScrollAmount();
                } else if ((border.bottom() - y) < buttonsize) {
                    dscroll += getScrollAmount();
                }
            }
            currentscrollpixels = (int) MathUtils.clampDouble(currentscrollpixels + dscroll, 0, getMaxScrollPixels());
        }
    }

    public void preRender(int mouseX, int mouseY, float partialTicks)  {
        border.draw();
        RenderState.glowOn();
        RenderState.texturelessOn();
        GlStateManager.glBegin(GL11.GL_TRIANGLES);
        Colour.LIGHTBLUE.doGL();
        // Can scroll down
        if (currentscrollpixels + border.height() < totalsize) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.bottom(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.bottom() - 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.bottom() - 4, 1);
        }
        // Can scroll up
        if (currentscrollpixels > 0) {
            GL11.glVertex3d(border.left() + border.width() / 2, border.top(), 1);
            GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.top() + 4, 1);
            GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.top() + 4, 1);
        }
        Colour.WHITE.doGL();
        GlStateManager.glEnd();
        RenderState.texturelessOff();
//        RenderState.scissorsOn(border.left() + 4, border.top() + 4, border.width() - 8, border.height() - 8);
        RenderState.scissorsOn(border.left(), border.top() + 4, border.width(), border.height() - 8); // get rid of margins
    }

    public void postRender(int mouseX, int mouseY, float partialTicks) {
        RenderState.scissorsOff();
        RenderState.glowOff();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        preRender(mouseX, mouseY, partialTicks);
        postRender(mouseX, mouseY, partialTicks);
    }

    public void frameOff() {
        this.disable();
        this.hide();
    }

    public void frameOn() {
        this.enable();
        this.show();
    }

    public void hide () {
        this.visibile = false;
    }

    public void show() {
        this.visibile = true;
    }

    public boolean isVisibile() {
        return this.visibile;
    }

    public void enable() {
        this.enabled = true;
    }

    public void disable() {
        this.enabled = false;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public Point2D getUpperLeft() {
        return new Point2D(border.finalLeft(), border.finalTop());
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    @Override
    public List<String> getToolTip(int x, int y) {
        return null;
    }

    @Override
    public IRect getBorder() {
        return this.border;
    }

    @Override
    public void setTargetDimensions(double left, double top, double right, double bottom) {
        border.setTargetDimensions(left, top, right, bottom);
    }

    @Override
    public void setTargetDimensions(Point2D ul, Point2D wh) {
        border.setTargetDimensions(ul, wh);
    }

    @Override
    public IRect setLeft(double value) {
        setLeft(value);
        return this;
    }

    @Override
    public IRect setRight(double value) {
        setRight(value);
        return this;
    }

    @Override
    public IRect setTop(double value) {
        setTop(value);
        return this;
    }

    @Override
    public IRect setBottom(double value) {
        setBottom(value);
        return this;
    }

    @Override
    public IRect setWidth(double value) {
        border.setWidth(value);
        return this;
    }

    @Override
    public IRect setHeight(double value) {
        border.setHeight(value);
        return this;
    }

    @Override
    public void move(Point2D moveAmount) {
        border.move(moveAmount);
    }

    @Override
    public void move(double x, double y) {
        border.move(x, y);
    }

    @Override
    public void setPosition(Point2D position) {
        border.setPosition(position);
    }
}
