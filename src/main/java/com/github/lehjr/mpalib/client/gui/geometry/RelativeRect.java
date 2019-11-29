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

public class RelativeRect extends Rect {
    protected Rect rectBelowMe;
    protected Rect rectAboveMe;
    protected Rect rectLeftOfMe;
    protected Rect rectRightOfMe;
    protected double leftPadding = 0;
    protected double topPadding = 0;
    protected double rightPadding = 0;
    protected double bottomPadding = 0;

    public RelativeRect(double left, double top, double right, double bottom) {
        super(left, top, right, bottom);
    }

    public RelativeRect(double left, double top, double right, double bottom, boolean growFromMiddle) {
        super(left, top, right, bottom, growFromMiddle);
    }

    public RelativeRect(Point2D ul, Point2D br) {
        super(ul, br);
    }

    /**
     * @return if there is a rectangle set left of this one,
     * returns the full grown right side of that plus padding
     */
    @Override
    public double left() {
        if(rectLeftOfMe != null) {
            return rectLeftOfMe.right() + leftPadding;
        }
       return ul.getX();
    }

    /**
     * @return if there is a rectangle set above this one,
     * returns the full grown bottom side of that plus padding
     */
    @Override
    public double top() {
        if (rectAboveMe != null) {
            return rectAboveMe.bottom() + topPadding;
        }
        return ul.getY();
    }

    /**
     * @return if there is a rectangle set right of this one,
     * returns the left side of that plus padding
     */
    @Override
    public double right() {
        if (rectRightOfMe != null) {
            return rectRightOfMe.left() + rightPadding;
        }
        return left() + wh.getX();
    }

    @Override
    public double bottom() {
        if (rectBelowMe != null) {
            return rectBelowMe.top() + bottomPadding;
        }
        return top() + wh.getY();
    }

    @Override
    public RelativeRect copyOf() {
        return new RelativeRect(this.left(), this.top(), this.right(), this.bottom(), (this.ul != this.ulFinal || this.wh != this.whFinal));
//                                .setBelow(this.belowme)
//                                .setAbove(this.aboveme)
//                                .setLeftOf(this.leftofme)
//                                .setRightOf(this.rightofme);
    }

    /**
     * Sets this rectangle left of another rectangle.
     * @param otherRightOfMe
     * @return this
     */
    public RelativeRect setMeLeftof(Rect otherRightOfMe) {
        this.rectRightOfMe = otherRightOfMe;
        return this;
    }

    /**
     * Sets this rectangle right of another rectangle
     * @param otherLeftOfMe
     * @return this
     */
    public RelativeRect setMeRightOf(Rect otherLeftOfMe) {
        this.rectLeftOfMe = otherLeftOfMe;
        return this;
    }

    /**
     * Sets this above another rectangle
     * @param otherBelowMe
     * @return this
     */
    public RelativeRect setMeAbove(Rect otherBelowMe) {
        this.rectBelowMe = otherBelowMe;
        return this;
    }

    /**
     * Sets this below another rectangle
     * @param otherAboveMe
     * @return this
     */
    public RelativeRect setMeBelow(Rect otherAboveMe) {
        this.rectAboveMe = otherAboveMe;
        return this;
    }
}