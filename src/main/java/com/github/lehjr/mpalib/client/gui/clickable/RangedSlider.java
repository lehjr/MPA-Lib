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

package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

/**
 * @author lehjr
 */
public class RangedSlider extends Clickable {
    final int cornersize = 3;

    private final double height = 16;
    protected double width;
    private String label;

    DrawableRect insideRect;
    DrawableRect outsideRect;

    public int id;

    /**
     * Is this slider control being dragged.
     */
    public boolean dragging = false;

    /**
     * The value of this slider control. Based on a value representing 0 - 100%
     */
    public double sliderValue = 1.0F;
    public double minValue = 0.0D;
    public double maxValue = 5.0D;

    @Nullable
    public ISlider parent = null;

    public RangedSlider(int id, Point2D position, double width, String label, double minVal, double maxVal, double currentVal) {
        this(id, position, width, label, minVal, maxVal, currentVal, null);
    }

    public RangedSlider(int id, Point2D position, String label, double minVal, double maxVal, double currentVal, ISlider par) {
        this(id, position, 150, label, minVal, maxVal, currentVal, par);
    }

    public RangedSlider(int id, Point2D position, double width, String label, double minVal, double maxVal, double currentVal, @Nullable ISlider iSlider) {
        this.width = width;
        this.id = id;
        this.position = position;
        this.label = label;
        createNewRects();
        minValue = minVal;
        maxValue = maxVal;
        sliderValue = (currentVal - minValue) / (maxValue - minValue);
        parent = iSlider;
    }

    void createNewRects() {
        this.insideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + height * 0.5, 0, position.getY() + height, Colour.ORANGE, Colour.LIGHTBLUE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + height * 0.5, position.getX() + width / 2.0 + cornersize, position.getY() + height, Colour.DARKBLUE, Colour.LIGHTBLUE);
        this.insideRect.setWidth(6);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible) {
            if (label != null) {
                Renderer.drawCenteredString(I18n.format(label), position.getX(), position.getY());
            }

            this.outsideRect.draw();
            this.insideRect.setPosition(new Point2D(this.position.getX() + this.width * (this.sliderValue - 0.5D), this.outsideRect.centery()));
            this.insideRect.draw();
        }
    }

    public void updateSlider() {
        this.sliderValue = MathUtils.clampDouble(sliderValue, 0.0D, 1.0D);

        if (parent != null) {
            parent.onChangeSliderValue(this);
        }
    }

    public int id() {
        return this.id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(position);
        createNewRects();
    }

    public void setWidth(double widthIn) {
        this.width = widthIn;
        createNewRects();
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */

    public void mouseReleased(int mouseX, int mouseY) {
        this.dragging = false;
    }

    public int getValueInt() {
        return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
    }

    public double getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }

    public void setValue(double d) {
        this.sliderValue = (d - minValue) / (maxValue - minValue);
    }

    @Override
    public boolean hitBox(double x, double y) {
        return Math.abs(position.getX() - x) < width / 2 &&
                Math.abs(position.getY() + 12 - y) < 4;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEnabled() && this.isVisible() && this.hitBox(mouseX, mouseY)) {
            this.sliderValue = MathUtils.clampDouble((mouseX - this.position.getX()) / (this.width -3) + 0.5, 0.0D, 1.0D);
            updateSlider();
            this.dragging = true;
            return true;
        }
        return false;
    }

    public interface ISlider {
        void onChangeSliderValue(RangedSlider slider);
    }
}
