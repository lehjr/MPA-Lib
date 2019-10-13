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

package com.github.lehjr.mpalib.client.gui.geometry;

public class FlyFromPointToPoint2D extends MusePoint2D {
    protected final MusePoint2D prev;
    protected final long spawnTime;
    protected final double timeTo;

    public FlyFromPointToPoint2D(double x, double y, double x2, double y2, double timeTo) {
        super(x2, y2);
        prev = new MusePoint2D(x, y);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
    }

    public FlyFromPointToPoint2D(MusePoint2D prev, MusePoint2D target, double timeTo) {
        this(prev.getX(), prev.getY(), target.getX(), target.getY(), timeTo);
    }

    @Override
    public double getX() {
        return doRatio(prev.x, x);
    }

    @Override
    public double getY() {
        return doRatio(prev.y, y);
    }

    public double doRatio(double val1, double val2) {
        long elapsed = System.currentTimeMillis() - spawnTime;
        double ratio = elapsed / timeTo;
        if (ratio > 1.0F) {
            return val2;
        } else {
            return val2 * ratio + val1 * (1 - ratio);
        }
    }
}