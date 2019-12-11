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

import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.geometry.Rect;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;

public class LabledButton extends Button {
    String label;

    public LabledButton(double left, double top, double right, double bottom, boolean growFromMiddle,
                        Colour insideColourEnabled,
                        Colour insideColourDisabled,
                        Colour outsideColourEnabled,
                        Colour outsideColourDisabled,
                        String label) {
        super(left, top, right, bottom, growFromMiddle, insideColourEnabled, insideColourDisabled, outsideColourEnabled, outsideColourDisabled);
        this.label = label;
    }

    public LabledButton(double left, double top, double right, double bottom,
                        Colour insideColourEnabled,
                        Colour insideColourDisabled,
                        Colour outsideColourEnabled,
                        Colour outsideColourDisabled,
                        String label) {
        super(left, top, right, bottom, insideColourEnabled, insideColourDisabled, outsideColourEnabled, outsideColourDisabled);
        this.label = label;
    }

    public LabledButton(Point2D ul, Point2D br,
                        Colour insideColourEnabled,
                        Colour insideColourDisabled,
                        Colour outsideColourEnabled,
                        Colour outsideColourDisabled,
                        String label) {
        super(ul, br, insideColourEnabled, insideColourDisabled, outsideColourEnabled, outsideColourDisabled);
        this.label = label;
    }

    public LabledButton(Rect ref,
                        Colour insideColourEnabled,
                        Colour insideColourDisabled,
                        Colour outsideColourEnabled,
                        Colour outsideColourDisabled,
                        String label) {
        super(ref, insideColourEnabled, insideColourDisabled, outsideColourEnabled, outsideColourDisabled);
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        super.render(mouseX, mouseY, partialTicks);
        if (isVisible) {
            Renderer.drawCenteredString(this.label, centerx(), centery() - 4);
        }
    }
}
