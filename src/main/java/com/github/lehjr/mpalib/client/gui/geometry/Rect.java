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

package com.github.lehjr.mpalib.client.gui.geometry;

public class Rect implements IRect {
    /** Note: separate "target values" are because window based sizes don't initialize properly in the constructor */
    /** target upper, left point */
    Point2D ulFinal;//
    /** target width and height */
    Point2D whFinal;
    /** top left origin */
    Point2D ul;
    /** width, height */
    Point2D wh;

    final boolean growFromMiddle;

    public Rect(double left, double top, double right, double bottom, boolean growFromMiddle) {
        ulFinal = new Point2D(left, top);
        whFinal = new Point2D(right - left, bottom - top);
        ul = ulFinal.copy();
        wh = whFinal.copy();
        this.growFromMiddle = growFromMiddle;
    }

    /**
     *  Alternative to spawning a completely new object. Especially handy for GUI's with large constructors
     */
    public void setTargetDimensions(double left, double top, double right, double bottom) {
        ulFinal = new Point2D(left, top);
        whFinal = new Point2D(right - left, bottom - top);
        grow();
    }

    public void setTargetDimensions(Point2D ul, Point2D wh) {
        ulFinal = ul;
        whFinal = wh;
        grow();
    }

    public Rect(double left, double top, double right, double bottom) {
        this(left, top, right, bottom, false);
    }

    public Rect(Point2D ul, Point2D br) {
        this.ulFinal = this.ul = ul;
        this.whFinal = this.wh = br.minus(ul);
        this.growFromMiddle = false;
    }

    /**
     * call after setTargetDimensions
     */
    void grow() {
        if (growFromMiddle) {
            Point2D center = ulFinal.plus(whFinal.times(0.5));
            this.ul = new FlyFromPointToPoint2D(center, ulFinal, 200);
            this.wh = new FlyFromPointToPoint2D(new Point2D(0, 0), whFinal, 200);
        } else {
            this.ul = this.ulFinal.copy();
            this.wh = this.whFinal.copy();
        }
    }

    public Rect copyOf() {
        return new Rect(this.left(), this.top(), this.right(), this.bottom(), (this.ul != this.ulFinal || this.wh != this.whFinal));
    }

    @Override
    public Point2D getUL() {
        return ul;
    }

    @Override
    public Point2D getULFinal() {
        return ulFinal;
    }

    @Override
    public Point2D getWH() {
        return wh;
    }

    @Override
    public Point2D getWHFinal() {
        return whFinal;
    }

    @Override
    public double left() {
        return ul.getX();
    }

    @Override
    public double finalLeft() {
        return ulFinal.getX();
    }

    @Override
    public double top() {
        return ul.getY();
    }

    @Override
    public double finalTop() {
        return ulFinal.getY();
    }

    @Override
    public double right() {
        return ul.getX() + wh.getX();
    }

    @Override
    public double finalRight() {
        return ulFinal.getX() + whFinal.getX();
    }

    @Override
    public double bottom() {
        return ul.getY() + wh.getY();
    }

    @Override
    public double finalBottom() {
        return ulFinal.getY() + whFinal.getY();
    }

    @Override
    public double width() {
        return wh.getX();
    }

    @Override
    public double finalWidth() {
        return whFinal.getY();
    }

    @Override
    public double height() {
        return wh.getY();
    }

    @Override
    public double finalHeight() {
        return whFinal.getY();
    }

    @Override
    public IRect setLeft(double value) {
        ul.setX(value);
        ulFinal.setX(value);
        return this;
    }

    @Override
    public IRect setRight(double value) {
        wh.setX(value - ul.getX());
        whFinal.setX(value - ulFinal.getX());
        return this;
    }

    @Override
    public IRect setTop(double value) {
        ul.setY(value);
        ulFinal.setY(value);
        return this;
    }

    @Override
    public IRect setBottom(double value) {
        wh.setY(value - ul.getY());
        whFinal.setY(value - ulFinal.getY());
        return this;
    }

    @Override
    public IRect setWidth(double value) {
        wh.setX(value);
        whFinal.setX(value);
        return this;
    }

    @Override
    public IRect setHeight(double value) {
        wh.y = value;
        whFinal.y = value;
        return this;
    }

    @Override
    public void move(Point2D moveAmount) {
        ulFinal = whFinal.plus(moveAmount);
        whFinal = whFinal.plus(moveAmount);
        grow();
    }

    @Override
    public void move(double x, double y) {
        ulFinal = whFinal.plus(x, y);
        whFinal = whFinal.plus(x, y);
        grow();
    }

    @Override
    public void setPosition(Point2D position) {
        ulFinal = position.minus(whFinal.times(0.5));
        grow();
    }

    @Override
    public boolean growFromMiddle() {
        return growFromMiddle;
    }

    // FIXME: needed?
//    public boolean equals(IRect other) {
//        return ul.equals(other.getUL()) &&
//                ulFinal.equals(other.getULFinal()) &&
//                wh.equals(other.getWH()) &&
//                whFinal.equals(other.getWHFinal());
//    }
}