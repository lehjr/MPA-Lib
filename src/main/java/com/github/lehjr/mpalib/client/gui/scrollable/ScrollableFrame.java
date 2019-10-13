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

package com.github.lehjr.mpalib.client.gui.scrollable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.github.lehjr.mpalib.client.gui.frame.IGuiFrame;
import com.github.lehjr.mpalib.client.gui.geometry.DrawableMuseRect;
import com.github.lehjr.mpalib.client.gui.geometry.MusePoint2D;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected final int buttonsize = 5;
    protected int totalsize;
    protected int currentscrollpixels;
    protected double lastdWheel = 0; //fixme
    protected boolean visibile = true;
    protected boolean enabled = true;

    protected DrawableMuseRect border;

    public ScrollableFrame(MusePoint2D topleft, MusePoint2D bottomright, Colour backgroundColour, Colour borderColour) {
        border = new DrawableMuseRect(topleft, bottomright, backgroundColour, borderColour);
    }

    protected double getScrollAmount() {
        return 8;
    }

    public void setTargetDimensions(double left, double top, double right, double bottom) {
        this.border.setTargetDimensions(left, top, right, bottom);
    }

    public void setTargetDimensions(MusePoint2D ul, MusePoint2D wh) {
        this.border.setTargetDimensions(ul, wh);
    }

    public void setTopLeft(MusePoint2D topLeft) {
        this.border.setTop(topLeft.getY());
        this.border.setLeft(topLeft.getX());
    }

    public void setBottomRight(MusePoint2D bottomRight) {
        this.border.setBottom(bottomRight.getY());
        this.border.setRight(bottomRight.getX());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dWheel) {
        if (border.containsPoint(mouseX, mouseY)) {
            // prevent negative total scroll values
            currentscrollpixels  = (int) MathUtils.clampDouble(currentscrollpixels-= dWheel * getScrollAmount(), 0, getMaxScrollPixels());
            return true;
        }
        return false;
    }

    @Override
    public void init(double left, double top, double right, double bottom) {
        this.border.setTargetDimensions(left, top, right, bottom);
    }

    @Override
    public void update(double x, double y) {

    }

    public void preRender(int mouseX, int mouseY, float partialTicks)  {
        border.draw();
        RenderState.glowOn();
        RenderState.texturelessOn();
        GlStateManager.begin(GL11.GL_TRIANGLES);
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
        GlStateManager.end();
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

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (this.border.containsPoint(x, y) && button == 0) {
            int dscroll = 0;
            if (y - this.border.top() < buttonsize && this.currentscrollpixels > 0) {
                dscroll = (int)((double)dscroll - this.getScrollAmount());
            } else if (this.border.bottom() - y < buttonsize) {
                dscroll = (int)((double)dscroll + this.getScrollAmount());
            }
            if (dscroll != 0) {
                this.currentscrollpixels = (int) MathUtils.clampDouble(this.currentscrollpixels + dscroll, 0.0D, this.getMaxScrollPixels());
                return true;
            }
        }
        return false;
    }

    public MusePoint2D getUpperLeft() {
        return new MusePoint2D(border.finalLeft(), border.finalTop());
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return false;
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        return null;
    }
}
