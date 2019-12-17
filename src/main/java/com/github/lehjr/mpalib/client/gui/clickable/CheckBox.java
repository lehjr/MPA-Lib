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
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.client.sound.Musique;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;

import java.util.List;

/**
 * @author lehjr
 */
public class CheckBox implements IClickable {
    protected IPressable onPressed;
    protected IReleasable onReleased;

    protected boolean isVisible;
    protected boolean isEnabled;

    protected boolean isChecked;
    protected DrawableTile tile;

    String label;

    public CheckBox(Point2D position, String displayString, boolean isChecked){
        Point2D ul = position.plus(4, 4);
        this.tile = new DrawableTile(ul, ul.plus(8, 8), Colour.BLACK, Colour.DARKGREY);
        this.label = displayString;
        this.isChecked = isChecked;
        this.enableAndShow();
    }

    public void setPosition(Point2D position) {
        Point2D ul = position.plus(4, 4);
        tile.setLeft(ul.getX());
        tile.setTop(ul.getY());
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if(this.isVisible) {
            tile.draw();
            if (isChecked) {
                Renderer.drawString("x", tile.centerx() - 2, tile.centery() - 5, Colour.WHITE);
            }
            Renderer.drawString(label, tile.centerx() + 8, tile.centery() - 4, Colour.WHITE);
        }
    }

    @Override
    public void move(Point2D point2D) {
        this.setPosition(getPosition().plus(point2D));
    }

    @Override
    public void move(double x, double y) {
        this.setPosition(getPosition().plus(x, y));
    }

    @Override
    public Point2D getPosition() {
        return tile.center();
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (this.isVisible() && this.isEnabled()) {
            return x >= this.tile.left() && x <= this.tile.right() && y >= this.tile.top() && y <= this.tile.bottom();
        } else {
            return false;
        }
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
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
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }

    @Override
    public void setOnPressed(IPressable iPressable) {
        this.onPressed = iPressable;
    }

    @Override
    public void setOnReleased(IReleasable iReleasable) {
        this.onReleased = iReleasable;
    }

    public void onReleased() {
        if (this.isVisible && this.isEnabled && this.onReleased != null) {
            this.onReleased.onReleased(this);
        }
    }

    public void onPressed() {
        if (this.isVisible && this.isEnabled) {
            Musique.playClientSound(SoundEvents.UI_BUTTON_CLICK, SoundCategory.MASTER, 1, Minecraft.getMinecraft().player.getPosition());
            this.isChecked = !this.isChecked;
            if (this.onPressed != null) {
                this.onPressed.onPressed(this);
            }
        }
    }
}
