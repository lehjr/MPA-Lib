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

package com.github.lehjr.mpalib.client.gui.frame;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected final int buttonsize = 5;
    protected int totalsize;
    protected int currentscrollpixels;
    protected boolean visible = true;
    protected boolean enabled = true;

    protected DrawableRect border;

    public ScrollableFrame(Point2D topleft, Point2D bottomright, Colour backgroundColour, Colour borderColour) {
        border = new DrawableRect(topleft, bottomright, backgroundColour, borderColour);
    }

    @Override
    public IRect getBorder() {
        return border;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible ;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            preRender(mouseX, mouseY, partialTicks);
            postRender(mouseX, mouseY, partialTicks);
        }
    }

    public void preRender(int mouseX, int mouseY, float partialTicks)  {
        if (isVisible()) {
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
    }

    public void postRender(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            RenderState.scissorsOff();
            RenderState.glowOff();
        }
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    protected double getScrollAmount() {
        return 8;
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
    public boolean mouseClicked(double x, double y, int button) {
        if (isVisible() && getBorder().containsPoint(x, y) && button == 0) {
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

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return false;
    }

    @Override
    public void update(double mouseX, double mouseY) {

    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        return null;
    }

    @Override
    public IRect setLeft(double value) {
        getBorder().setLeft(value);
        return this;
    }

    @Override
    public IRect setRight(double value) {
        getBorder().setRight(value);
        return this;
    }

    @Override
    public IRect setTop(double value) {
        getBorder().setTop(value);
        return this;
    }

    @Override
    public IRect setBottom(double value) {
        getBorder().setBottom(value);
        return this;
    }

    @Override
    public IRect setWidth(double value) {
        getBorder().setWidth(value);
        return this;
    }

    @Override
    public IRect setHeight(double value) {
        getBorder().setHeight(value);
        return this;
    }
}
