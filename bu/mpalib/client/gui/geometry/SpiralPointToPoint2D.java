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

public class SpiralPointToPoint2D extends Point2D {
    protected final Point2D center;
    protected final long spawnTime;
    protected final double timeTo;
    protected final boolean outwards;
    protected final double spiral = Math.PI;
    protected double radius;
    protected double rotation;

    public SpiralPointToPoint2D(double x, double y,
                                double x2, double y2,
                                double timeTo, boolean outwards) {
        super(x2, y2);
        center = new Point2D(x, y);
        this.radius = this.distanceTo(center);
        this.rotation = Math.atan2(y2 - y, x2 - x);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
        this.outwards = outwards;
    }

    public SpiralPointToPoint2D(double x, double y,
                                double x2, double y2,
                                double timeTo) {
        this(x, y, x2, y2, timeTo, true);
    }

    public SpiralPointToPoint2D(Point2D center,
                                Point2D target,
                                double timeTo, boolean outwards) {
        this(center.getX(), center.getY(), target.getX(), target.getY(), timeTo, outwards);
    }

    public SpiralPointToPoint2D(Point2D center,
                                Point2D target,
                                double timeTo) {
        this(center, target, timeTo, true);
    }

    public SpiralPointToPoint2D(Point2D center,
                                double radius, double rotation,
                                double timeTo, boolean outward) {
        this(center.getX(), center.getY(), radius * Math.cos(rotation), radius * Math.sin(rotation), timeTo, outward);
        this.radius = radius;
        this.rotation = rotation;
    }

    public SpiralPointToPoint2D(Point2D center,
                                double radius, double rotation,
                                double timeTo) {
        this(center, radius, rotation, timeTo, true);
    }

    private double getRatio() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        double ratio = elapsed / timeTo;
        if (ratio > 1.0D) {
            ratio = 1.0D;
        }
        if (outwards) {
            return ratio;
        } else {
            return 1.0D - ratio;
        }
    }

    private double getTheta() {
        return rotation + (spiral * (1.0D - getRatio()));
    }

    @Override
    public double getX() {
        //getX = r × cos(θ)
        return center.getX() + (radius * getRatio()) * Math.cos(getTheta());
    }

    @Override
    public double getY() {
        //getY = r × sin(θ)
        return center.getY() + (radius * getRatio()) * Math.sin(getTheta());
    }

    @Override
    public String toString() {
        return "\ntarget.X: " + x + ", target.Y: " + y +
                "\nactualX: " + getX() + "actualX: " + getY() +
                "\nrotation: " + rotation +
                "\nradius: " + radius;
    }
}

//    return ((3 * Math.PI / 2) - ((2 * Math.PI * modeNum) / totalModes) + (2 * Math.PI * ((1 - (frame / numFrames))));
//                   ^--------rotation----------------^