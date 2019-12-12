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

import com.github.lehjr.mpalib.client.gui.geometry.DrawableTile;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.geometry.Rect;
import com.github.lehjr.mpalib.math.Colour;

import java.util.List;

public class Button implements IClickable {
    protected IPressable onPressed;
    protected IReleasable onReleased;
    protected DrawableTile tile;
    protected boolean isEnabled = true;
    protected boolean isVisible = true;
    Colour backgroundColourEnabled;
    Colour backgroundColourDisabled;
    Colour borderColourEnabled;
    Colour borderColourDisabled;

    int highlight = 5;

    public Button(double left, double top, double right, double bottom, boolean growFromMiddle,
                  Colour backgroundColourEnabled,
                  Colour backgroundColourDisabled,
                  Colour borderColourEnabled,
                  Colour borderColourDisabled) {
        this.tile = new DrawableTile(left, top, right, bottom, growFromMiddle, backgroundColourEnabled, borderColourEnabled);
        this.backgroundColourEnabled = backgroundColourEnabled;
        this.backgroundColourDisabled = backgroundColourDisabled;
        this.borderColourEnabled = borderColourEnabled;
        this.borderColourDisabled = borderColourDisabled;
    }

    public Button(double left, double top, double right, double bottom,
                  Colour backgroundColourEnabled,
                  Colour backgroundColourDisabled,
                  Colour borderColourEnabled,
                  Colour borderColourDisabled) {
        this.tile = new DrawableTile(left, top, right, bottom, backgroundColourEnabled, borderColourEnabled);
        this.backgroundColourEnabled = backgroundColourEnabled;
        this.backgroundColourDisabled = backgroundColourDisabled;
        this.borderColourEnabled = borderColourEnabled;
        this.borderColourDisabled = borderColourDisabled;
    }

    public Button(Point2D ul, Point2D br,
                  Colour backgroundColourEnabled,
                  Colour backgroundColourDisabled,
                  Colour borderColourEnabled,
                  Colour borderColourDisabled) {
        this.tile = new DrawableTile(ul, br, backgroundColourEnabled, borderColourEnabled);
        this.backgroundColourEnabled = backgroundColourEnabled;
        this.backgroundColourDisabled = backgroundColourDisabled;
        this.borderColourEnabled = borderColourEnabled;
        this.borderColourDisabled = borderColourDisabled;
    }

    public Button(Rect ref,
                  Colour backgroundColourEnabled,
                  Colour backgroundColourDisabled,
                  Colour borderColourEnabled,
                  Colour borderColourDisabled) {
        this.tile = new DrawableTile(ref.finalLeft(), ref.finalTop(), ref.finalRight(), ref.finalBottom(), backgroundColourEnabled, borderColourEnabled);
        this.backgroundColourEnabled = backgroundColourEnabled;
        this.backgroundColourDisabled = backgroundColourDisabled;
        this.borderColourEnabled = borderColourEnabled;
        this.borderColourDisabled = borderColourDisabled;
    }

    @Override
    public void setOnPressed(IPressable onPressed) {
        this.onPressed = onPressed;
    }

    @Override
    public void setOnReleased(IReleasable onReleased) {
        this.onReleased = onReleased;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isVisible) {
            if (isEnabled()) {
                if (hitBox(mouseX, mouseY)) {
                    this.tile.setBackgroundColour(new Colour(
                            (byte)(this.backgroundColourEnabled.r + highlight < 255 ? this.backgroundColourEnabled.r + highlight : 255),
                            (byte)(this.backgroundColourEnabled.g + highlight < 255 ? this.backgroundColourEnabled.g + highlight : 255),
                            (byte)(this.backgroundColourEnabled.b + highlight < 255 ? this.backgroundColourEnabled.b + highlight : 255),
                            1));
                } else {
                    this.tile.setBackgroundColour(this.backgroundColourEnabled);
                }
                this.tile.setBorderColour(this.borderColourEnabled);
            } else {
                this.tile.setBackgroundColour(backgroundColourDisabled);
                this.tile.setBorderColour(this.borderColourDisabled);
            }
            this.tile.draw();
        }
    }

    public void setTargetDimensions(double left, double top, double right, double bottom) {
        this.tile.setTargetDimensions(left, top, right, bottom);
    }

    public void setTargetDimensions(Point2D ul, Point2D wh) {
        this.tile.setTargetDimensions(ul, wh);
    }

    public double left() {
        return this.tile.left();
    }

    public double finalLeft() {
        return this.tile.finalLeft();
    }

    public double top() {
        return this.tile.top();
    }

    public double finalTop() {
        return this.tile.finalTop();
    }

    public double right() {
        return this.tile.right();
    }

    public double finalRight() {
        return this.tile.finalRight();
    }

    public double bottom() {
        return this.tile.bottom();
    }

    public double finalBottom() {
        return this.tile.finalBottom();
    }

    public double width() {
        return this.tile.width();
    }

    public double finalWidth() {
        return this.tile.finalWidth();
    }

    public double height() {
        return this.tile.height();
    }

    public double finalHeight() {
        return this.tile.finalHeight();
    }

    public void setLeft(double value) {
        tile.setLeft(value);
    }

    public void setRight(double value) {
        this.tile.setRight(value);
    }

    public void setTop(double value) {
        this.tile.setTop(value);
    }

    public void setBottom(double value) {
        this.tile.setBottom(value);
    }

    public void setWidth(double value) {
        this.tile.setWidth(value);
    }

    public void setHeight(double value) {
        this.tile.setHeight(value);
    }

    @Override
    public void move(Point2D position) {
        this.tile.move(position);
    }

    @Override
    public void move(double x, double y) {
        this.tile.move(x, y);
    }

    @Override
    public void setPosition(Point2D position) {
        this.tile.setPosition(position);
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (isVisible() && isEnabled())
            return x >= this.tile.left() && x <= this.tile.right() && y >= this.tile.top() && y <= this.tile.bottom();
        return false;
    }

    @Override
    public Point2D getPosition() {
        return this.tile.center();
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void onReleased() {
        if (this.isVisible && this.isEnabled && this.onReleased != null) {
            this.onReleased.onReleased(this);
        }
    }

    @Override
    public void onPressed() {
        if (this.isVisible && this.isEnabled && this.onPressed != null) {
            this.onPressed.onPressed(this);
        }
    }
}