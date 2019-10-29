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

public class Rect {
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

    public Point2D center() {
        return new Point2D(centerx(), centery());
    }

    public double left() {
        return ul.getX();
    }

    public double finalLeft() {
        return ulFinal.getX();
    }

    public double top() {
        return ul.getY();
    }

    public double finalTop() {
        return ulFinal.getY();
    }

    public double right() {
        return ul.getX() + wh.getX();
    }

    public double finalRight() {
        return ulFinal.getX() + whFinal.getX();
    }

    public double bottom() {
        return ul.getY() + wh.getY();
    }

    public double finalBottom() {
        return ulFinal.getY() + whFinal.getY();
    }

    public double width() {
        return wh.getX();
    }

    public double finalWidth() {
        return whFinal.getY();
    }

    public double height() {
        return wh.getY();
    }

    public double finalHeight() {
        return whFinal.getY();
    }

    public Rect setLeft(double value) {
        ul.x = value;
        ulFinal.x =value;
        return this;
    }

    public Rect setRight(double value) {
        wh.x = value - ul.getX();
        whFinal.x = value - ulFinal.getX();
        return this;
    }

    public Rect setTop(double value) {
        ul.y = value;
        ulFinal.y = value;
        return this;
    }

    public Rect setBottom(double value) {
        wh.y = value - ul.getY();
        whFinal.y = value - ulFinal.getY();
        return this;
    }

    public Rect setWidth(double value) {
        wh.x = value;
        whFinal.x = value;
        return this;
    }

    public Rect setHeight(double value) {
        wh.y = value;
        whFinal.y = value;
        return this;
    }

    public void move(Point2D moveAmount) {
        ulFinal = whFinal.plus(moveAmount);
        whFinal = whFinal.plus(moveAmount);
        grow();
    }

    public boolean growFromMiddle() {
        return growFromMiddle;
    }

    public boolean equals(Rect other) {
        return ul.equals(other.ul) && wh.equals(other.wh);
    }

    public boolean containsPoint(double x, double y) {
        return x > left() && x < right() && y > top() && y < bottom();
    }

    public double centerx() {
        return (left() + right()) / 2.0;
    }

    public double centery() {
        return (top() + bottom()) / 2.0;
    }
}