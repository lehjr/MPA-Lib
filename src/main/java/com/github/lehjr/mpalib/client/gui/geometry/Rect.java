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

package com.github.lehjr.mpalib.client.gui.geometry;

public class Rect implements IRect {
    /** Note: separate "target values" are because window based sizes don't initialize properly in the constructor */
    /** target upper, left point */
    Point2F ulFinal;//
    /** target width and height */
    Point2F whFinal;
    /** top left origin */
    Point2F ul;
    /** width, height */
    Point2F wh;

    final boolean growFromMiddle;

    public Rect(float left, float top, float right, float bottom, boolean growFromMiddle) {
        ulFinal = new Point2F(left, top);
        whFinal = new Point2F(right - left, bottom - top);
        ul = ulFinal.copy();
        wh = whFinal.copy();
        this.growFromMiddle = growFromMiddle;
    }

    /**
     *  Alternative to spawning a completely new object. Especially handy for GUI's with large constructors
     */
    public void setTargetDimensions(float left, float top, float right, float bottom) {
        ulFinal = new Point2F(left, top);
        whFinal = new Point2F(right - left, bottom - top);
        grow();
    }

    public void setTargetDimensions(Point2F ul, Point2F wh) {
        ulFinal = ul;
        whFinal = wh;
        grow();
    }

    public Rect(float left, float top, float right, float bottom) {
        this(left, top, right, bottom, false);
    }

    public Rect(Point2F ul, Point2F br, boolean growFromMiddle) {
        this.ulFinal = this.ul = ul;
        this.whFinal = this.wh = br.minus(ul);
        this.growFromMiddle = growFromMiddle;
    }

    public Rect(Point2F ul, Point2F br) {
        this.ulFinal = this.ul = ul;
        this.whFinal = this.wh = br.minus(ul);
        this.growFromMiddle = false;
    }

    /**
     * call after setTargetDimensions
     */
    void grow() {
        if (growFromMiddle) {
            Point2F center = ulFinal.plus(whFinal.times(0.5F));
            this.ul = new FlyFromPointToPoint2F(center, ulFinal, 200);
            this.wh = new FlyFromPointToPoint2F(new Point2F(0, 0), whFinal, 200);
        } else {
            this.ul = this.ulFinal.copy();
            this.wh = this.whFinal.copy();
        }
    }

    public Rect copyOf() {
        return new Rect(this.left(), this.top(), this.right(), this.bottom(), (this.ul != this.ulFinal || this.wh != this.whFinal));
    }

    @Override
    public Point2F getUL() {
        return ul;
    }

    @Override
    public Point2F getULFinal() {
        return ulFinal;
    }

    @Override
    public Point2F getWH() {
        return wh;
    }

    @Override
    public Point2F getWHFinal() {
        return whFinal;
    }

    @Override
    public float left() {
        return ul.getX();
    }

    @Override
    public float finalLeft() {
        return ulFinal.getX();
    }

    @Override
    public float top() {
        return ul.getY();
    }

    @Override
    public float finalTop() {
        return ulFinal.getY();
    }

    @Override
    public float right() {
        return ul.getX() + wh.getX();
    }

    @Override
    public float finalRight() {
        return ulFinal.getX() + whFinal.getX();
    }

    @Override
    public float bottom() {
        return ul.getY() + wh.getY();
    }

    @Override
    public float finalBottom() {
        return ulFinal.getY() + whFinal.getY();
    }

    @Override
    public float width() {
        return wh.getX();
    }

    @Override
    public float finalWidth() {
        return whFinal.getX();
    }

    @Override
    public float height() {
        return wh.getY();
    }

    @Override
    public float finalHeight() {
        return whFinal.getY();
    }

    @Override
    public IRect setLeft(float value) {
        ul.setX(value);
        ulFinal.setX(value);
        return this;
    }

    @Override
    public IRect setRight(float value) {
        wh.setX(value - ul.getX());
        whFinal.setX(value - ulFinal.getX());
        return this;
    }

    @Override
    public IRect setTop(float value) {
        ul.setY(value);
        ulFinal.setY(value);
        return this;
    }

    @Override
    public IRect setBottom(float value) {
        wh.setY(value - ul.getY());
        whFinal.setY(value - ulFinal.getY());
        return this;
    }

    @Override
    public IRect setWidth(float value) {
        wh.setX(value);
        whFinal.setX(value);
        return this;
    }

    @Override
    public IRect setHeight(float value) {
        wh.setY(value);
        whFinal.setY(value);
        return this;
    }

    @Override
    public void move(Point2F moveAmount) {
        ulFinal = whFinal.plus(moveAmount);
        whFinal = whFinal.plus(moveAmount);
        grow();
    }

    @Override
    public void move(float x, float y) {
        ulFinal = whFinal.plus(x, y);
        whFinal = whFinal.plus(x, y);
        grow();
    }

    @Override
    public void setPosition(Point2F position) {
        ulFinal = position.minus(whFinal.times(0.5F));
        grow();
    }

    @Override
    public boolean growFromMiddle() {
        return growFromMiddle;
    }
}