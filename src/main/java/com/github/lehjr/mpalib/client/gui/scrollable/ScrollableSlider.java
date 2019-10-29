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

package com.github.lehjr.mpalib.client.gui.scrollable;

import com.github.lehjr.mpalib.client.gui.clickable.ClickableSlider;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.geometry.RelativeRect;

public class ScrollableSlider extends ScrollableRectangle {
    ClickableSlider slider;

    public ScrollableSlider(ClickableSlider slider, RelativeRect relativeRect) {
        super(relativeRect);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
        this.slider = slider;
    }

    public ScrollableSlider(ClickableSlider slider, Point2D ul, Point2D br) {
        super(ul, br);
        this.slider = slider;
    }

    public double getValue() {
        return slider.getValue();
    }

    public void setValue(double value) {
        slider.setValue(value);
    }

    public ClickableSlider getSlider() {
        return slider;
    }

    public boolean hitBox(double x, double y) {
        return slider.hitBox(x, y);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        slider.render(mouseX, mouseY, partialTicks);
    }
}