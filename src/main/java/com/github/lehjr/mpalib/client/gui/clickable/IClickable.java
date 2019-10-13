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

package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.MusePoint2D;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

public interface IClickable {
    void render(int mouseX, int mouseY, float partialTicks);

    void move(double x, double y);

    default void move(MusePoint2D position) {
        this.move(position.getX(), position.getY());
    }

    MusePoint2D getPosition();

    boolean hitBox(double x, double y);

    List<ITextComponent> getToolTip();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void setVisible(boolean visible);

    boolean isVisible();

    default void hide() {
        setVisible(false);
    }

    default void show() {
        setVisible(true);
    }

    default void enable() {
        setEnabled(true);
    }

    default void disable() {
        setEnabled(false);
    }

    default void enableAndShow() {
        enable();
        show();
    }

    default void disableAndHide() {
        disable();
        hide();
    }

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(hitBox(mouseX, mouseY) && this.isEnabled() && this.isVisible()) {
            InputMappings.Input mouseKey = InputMappings.Type.MOUSE.getOrMakeInput(button);
            boolean flag = Minecraft.getInstance().gameSettings.keyBindPickBlock.isActiveAndMatches(mouseKey);

            if (button == 0 || button == 1 || flag) {
                this.onPressed();
            }
        }
        return false;
    }

    void setOnPressed(IPressable onPressed);

    void setOnReleased(IReleasable onReleased);

    void onPressed();

    void onReleased();

    @OnlyIn(Dist.CLIENT)
    interface IPressable {
        void onPressed(IClickable doThis);
    }

    @OnlyIn(Dist.CLIENT)
    interface IReleasable {
        void onReleased(IClickable doThis);
    }
}