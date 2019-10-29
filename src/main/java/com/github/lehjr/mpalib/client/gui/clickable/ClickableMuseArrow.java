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

package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableArrow;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.geometry.RelativeRect;
import com.github.lehjr.mpalib.math.Colour;

import java.util.List;

public class ClickableMuseArrow extends DrawableArrow implements IClickable {
    protected IPressable onPressed;
    protected IReleasable onReleased;

    protected final Colour backgroundColourNotHighlighted;
    protected final Colour backgroundColourHighlighted;

    protected boolean isEnabled = true;
    protected boolean isVisible = true;

    public ClickableMuseArrow(double left, double top, double right, double bottom, boolean growFromMiddle, Colour backgroundColour, Colour backgroundColourHighlighted, Colour borderColour) {
        super(left, top, right, bottom, growFromMiddle, backgroundColour, borderColour);
        this.backgroundColourNotHighlighted = backgroundColour;
        this.backgroundColourHighlighted = backgroundColourHighlighted;
    }

    public ClickableMuseArrow(double left, double top, double right, double bottom, Colour backgroundColour, Colour backgroundColourHighlighted, Colour borderColour) {
        super(left, top, right, bottom, backgroundColour, borderColour);
        this.backgroundColourNotHighlighted = backgroundColour;
        this.backgroundColourHighlighted = backgroundColourHighlighted;
    }

    public ClickableMuseArrow(Point2D ul, Point2D br, Colour backgroundColour, Colour backgroundColourHighlighted, Colour borderColour) {
        super(ul, br, backgroundColour, borderColour);
        this.backgroundColourNotHighlighted = backgroundColour;
        this.backgroundColourHighlighted = backgroundColourHighlighted;
    }

    public ClickableMuseArrow(RelativeRect ref, Colour backgroundColour, Colour backgroundColourHighlighted, Colour borderColour) {
        super(ref, backgroundColour, borderColour);
        this.backgroundColourNotHighlighted = backgroundColour;
        this.backgroundColourHighlighted = backgroundColourHighlighted;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isVisible) {
            super.setBackgroundColour(hitBox(mouseX, mouseY) ? this.backgroundColourHighlighted : this.backgroundColourNotHighlighted);
            this.draw();
        }
    }

    @Override
    public void move(double x, double y) {
        setLeft(x - width()/2);
        setRight(y - height()/2);
    }

    @Override
    public Point2D getPosition() {
        return new Point2D(centerx(), centery());
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (isVisible() && isEnabled())
            return x >= left() && x <= right() && y >= top() && y <= bottom();
        return false;
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }

    public void setOnPressed(IPressable onPressed) {
        this.onPressed = onPressed;
    }

    public void setOnReleased(IReleasable onReleased) {
        this.onReleased = onReleased;
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
