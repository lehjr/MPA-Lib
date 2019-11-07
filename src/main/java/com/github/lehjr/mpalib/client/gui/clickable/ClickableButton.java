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
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.renderer.RenderHelper;

/**
 * @author MachineMuse
 */
public class ClickableButton extends Clickable {
    protected String label;
    protected Point2D radius;
    protected DrawableRect rect;
    private final Colour enabledBorder  = new Colour(0.3F, 0.3F, 0.3F, 1);
    private final Colour enabledBackground = new Colour(0.5F, 0.6F, 0.8F, 1);
    private final Colour disabledBorder = new Colour(0.8F, 0.6F, 0.6F, 1);
    private final Colour disabledBackground = new Colour(0.8F, 0.3F, 0.3F, 1);

    public ClickableButton(String label, Point2D position, boolean enabled) {
        this.label = label;
        this.position = position;

        if (label.contains("\n")) {
            String[] x = label.split("\n");

            int longestIndex = 0;
            for (int i = 0; i < x.length; i++) {
                if (x[i].length() > x[longestIndex].length())
                    longestIndex = i;
            }
            this.radius = new Point2D(Renderer.getStringWidth(x[longestIndex]) / 2 + 2, 6 * x.length);
        } else {
            this.radius = new Point2D(Renderer.getStringWidth(label) / 2 + 2, 6);
        }

        this.rect = new DrawableRect(
                position.getX() - radius.getX(),
                position.getY() - radius.getY(),
                position.getX() + radius.getX(),
                position.getY() + radius.getY(),
                enabledBorder,
                enabledBackground
        );
        this.setEnabled(enabled);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            this.rect.setLeft(position.getX() - radius.getX());
            this.rect.setTop(position.getY() - radius.getY());
            this.rect.setRight(position.getX() + radius.getX());
            this.rect.setBottom(position.getY() + radius.getY());
            this.rect.setBorderColour(isEnabled() ? enabledBorder : disabledBorder);
            this.rect.setBackgroundColour(isEnabled() ? enabledBackground : disabledBackground);
            this.rect.draw();
            // standardItemLighting calls to fix issues with container slot highlighting causing issues with this
            RenderHelper.disableStandardItemLighting();
            if (label.contains("\n")) {
                String[] s = label.split("\n");
                for (int i = 0; i < s.length; i++) {
                    Renderer.drawCenteredString(s[i], position.getX(), position.getY() - (4 * s.length) + (i * 8));
                }
            } else {
                Renderer.drawCenteredString(this.label, position.getX(), position.getY() - 4);
            }
            RenderHelper.enableGUIStandardItemLighting();
        }
    }

    public Point2D getRadius () {
        return radius.copy();
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (!enabled) {
            return false;
        }
        boolean hitx = Math.abs(position.getX() - x) < radius.getX();
        boolean hity = Math.abs(position.getY() - y) < radius.getY();
        return hitx && hity;
    }

    public ClickableButton setLable(String label) {
        this.label = label;
        return this;
    }

    public String getLabel() {
        return label;
    }
}
