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

/**
 * Base class for Points. The main reason for this is to have a
 * pass-by-reference coordinate with getter/setter functions so that points with
 * more elaborate behaviour can be implemented - such as for open/close
 * animations.
 *
 * @author MachineMuse
 */
public class Point2D {
    protected double x;
    protected double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D p) {
        this(p.x, p.y);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Point2D plus(Point2D b) {
        return new Point2D(getX() + b.getX(), getY() + b.getY());
    }

    public Point2D plus(double x, double y) {
        return new Point2D(getX() + x, getY() + y);
    }

    public Point2D minus(Point2D b) {
        return new Point2D(getX() - b.getX(), getY() - b.getY());
    }

    public Point2D minus(double x, double y) {
        return new Point2D(getX() - x, getY() - y);
    }

    public Point2D times(double scalefactor) {
        return new Point2D(getX() * scalefactor, getY() * scalefactor);
    }

    public boolean equals(Point2D other) {
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    public double distance() {
        return Math.sqrt(getX() * getX() + getY() * getY());
    }

    public double distanceTo(Point2D position) {
        return Math.sqrt(distanceSq(position));
    }

    public double distanceSq(Point2D position) {
        double xdist = position.getX() - this.getX();
        double ydist = position.getY() - this.getY();
        return xdist * xdist + ydist * ydist;
    }

    public Point2D normalize() {
        double distance = distance();
        return new Point2D(getX() / distance, getY() / distance);
    }

    public Point2D midpoint(Point2D target) {
        return new Point2D((this.getX() + target.getX()) / 2, (this.getY() + target.getY()) / 2);
    }

    public Point2D copy() {
        return new Point2D(this.getX(), this.getY());
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}
