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

package com.github.lehjr.mpalib.client.gui.frame;

import com.github.lehjr.mpalib.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;

import java.util.List;

public interface IGuiFrame extends IRect {
    /**
     * @param mouseX
     * @param mouseY
     * @param button
     */
    boolean onMouseDown(double mouseX, double mouseY, int button);

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     * @return true if mouse release is inside this frame and is handled here, else false
     */
    boolean onMouseUp(double mouseX, double mouseY, int button);

    /**
     * Fired when gui init is fired, during the creation phase and on resize. Can be used to setup the frame
     * including setting target dimensions.
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void init(double left, double top, double right, double bottom);

    /**
     * Called in the render loop before rendering. Use to update this frame
     * @param mouseX
     * @param mouseY
     */
    void update(double mouseX, double mouseY);

    /**
     * Render elements of this frame. Ordering is important.
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    void render(int mouseX, int mouseY, float partialTicks);

    /**
     *
     * @param x mouseX
     * @param y mouseY
     * @return tooltip or null if not returning tooltip;
     */
    List<String> getToolTip(int x, int y);


    IRect getBorder();

    /** IRect for easier placement data and manipulation ----------------------------------------------------------- */

    @Override
    default Point2D center() {
        return getBorder().center();
    }

    @Override
    default Point2D getUL() {
        return getBorder().getUL();
    }

    @Override
    default Point2D getULFinal() {
        return getBorder().getULFinal();
    }

    @Override
    default Point2D getWH() {
        return getBorder().getWH();
    }

    @Override
    default Point2D getWHFinal() {
        return getBorder().getWHFinal();
    }

    @Override
    default double left() {
        return getBorder().left();
    }

    @Override
    default double finalLeft() {
        return getBorder().finalLeft();
    }

    @Override
    default double top() {
        return getBorder().top();
    }

    @Override
    default double finalTop() {
        return getBorder().finalTop();
    }

    @Override
    default double right() {
        return getBorder().right();
    }

    @Override
    default double finalRight() {
        return getBorder().finalRight();
    }

    @Override
    default double bottom() {
        return getBorder().bottom();
    }

    @Override
    default double finalBottom() {
        return getBorder().finalBottom();
    }

    @Override
    default double width() {
        return getBorder().width();
    }

    @Override
    default double finalWidth() {
        return getBorder().finalWidth();
    }

    @Override
    default double height() {
        return getBorder().height();
    }

    @Override
    default double finalHeight() {
        return getBorder().finalHeight();
    }

    @Override
    default boolean growFromMiddle() {
        return getBorder().growFromMiddle();
    }

    @Override
    default double centerx() {
        return getBorder().centerx();
    }

    @Override
    default double centery() {
        return getBorder().centery();
    }
}