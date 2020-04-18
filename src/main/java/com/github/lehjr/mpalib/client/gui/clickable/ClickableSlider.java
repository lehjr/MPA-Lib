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

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2F;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.resources.I18n;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:08 AM, 06/05/13
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 *
 * TODO: revisit and rewrite
 *
 */
public class ClickableSlider extends Clickable {
    final int cornersize = 3;
    private float valueInternal = 0;
    Point2F pos;
    float width;
    private String id;
    private String label;
    DrawableRect insideRect;
    DrawableRect outsideRect;
    boolean isEnabled = true;
    boolean isVisible = true;

    public ClickableSlider(Point2F pos, float width, String id, String label) {
        this.pos = pos;
        this.width = width;
        this.id = id;
        this.position = pos;
        this.insideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + 8, 0, position.getY() + 16, Colour.ORANGE, Colour.LIGHTBLUE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + 8, position.getX() + width / 2.0F + cornersize, position.getY() + 16, Colour.DARKBLUE, Colour.LIGHTBLUE);
        this.label = label;
    }

    public String id() {
        return this.id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, float zLevel) {
        Renderer.drawCenteredString(I18n.format(label), position.getX(), position.getY());
        this.insideRect.setRight(position.getX() + width * (getValue() - 0.5F) + cornersize);
        this.outsideRect.draw(zLevel);
        this.insideRect.draw(zLevel);
    }

    @Override
    public boolean hitBox(float x, float y) {
        return Math.abs(position.getX() - x) < width / 2 &&
                Math.abs(position.getY() + 12 - y) < 4;
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
    public void onPressed() {
        // TODO: this
    }

    public float getValue() {
        return valueInternal;
    }

    public void setValue(float v) {
        valueInternal = v;
    }

    public void setValueByX(float x) {
        valueInternal = MathUtils.clampFloat((x - pos.getX()) / width + 0.5F, 0, 1);
    }
}