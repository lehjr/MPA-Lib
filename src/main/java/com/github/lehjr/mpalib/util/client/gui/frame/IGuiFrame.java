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

package com.github.lehjr.mpalib.util.client.gui.frame;

import com.github.lehjr.mpalib.util.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public interface IGuiFrame extends IRect {
    /**
     * @param mouseX
     * @param mouseY
     * @param button
     * @return true if mouse click is inside this frame and is handled here, else false
     */
    boolean mouseClicked(double mouseX, double mouseY, int button);

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     * @return true if mouse release is inside this frame and is handled here, else false
     */
    boolean mouseReleased(double mouseX, double mouseY, int button);

    /**
     * @param mouseX
     * @param mouseY
     * @param dWheel
     * @return true if mouse pointer is inside this frame and scroll is handled here, else false
     */
    boolean mouseScrolled(double mouseX, double mouseY, double dWheel);

    /**
     * Fired when gui init is fired, during the creation phase and on resize. Can be used to setup the frame
     * including setting target dimensions.
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    default void init(double left, double top, double right, double bottom) {
        getBorder().setTargetDimensions(left, top, right, bottom);
    }

    /**
     * Called in the render loop before rendering. Use to update this frame
     *
     * @param mouseX
     * @param mouseY
     */
    void update(double mouseX, double mouseY);

    /**
     * Render elements of this frame. Ordering is important.
     *
     * @param matrixStack
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks);

    /**
     * @param x mouseX
     * @param y mouseY
     * @return tooltip or null if not returning tooltip;
     */
    List<ITextComponent> getToolTip(int x, int y);

    IRect getBorder();

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

    /**
     * IRect for easier placement data and manipulation -----------------------------------------------------------
     */
    @Override
    default void setTargetDimensions(double left, double top, double right, double bottom) {
        getBorder().setTargetDimensions(left, top, right, bottom);
    }

    @Override
    default void setTargetDimensions(Point2D ul, Point2D wh) {
        getBorder().setTargetDimensions(ul.copy(), wh.copy());
    }

    @Override
    default void move(Point2D moveAmount) {
        getBorder().move(moveAmount.copy());
    }

    @Override
    default void move(double x, double y) {
        getBorder().move(x, y);
    }

    @Override
    default void setPosition(Point2D position) {
        getBorder().setPosition(position.copy());
    }

    @Override
    default boolean containsPoint(double x, double y) {
        return getBorder().containsPoint(x, y);
    }

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