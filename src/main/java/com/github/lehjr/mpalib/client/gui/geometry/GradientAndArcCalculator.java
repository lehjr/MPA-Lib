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

import com.github.lehjr.mpalib.math.Colour;
import org.lwjgl.BufferUtils;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:52 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/10/16.
 */
public class GradientAndArcCalculator {
    /**
     * Efficient algorithm for drawing circles and arcs in pure opengl!
     *
     * @param startangle Start angle in radians
     * @param endangle   End angle in radians
     * @param radius     Radius of the circle (used in calculating number of segments to draw as well as size of the arc)
     * @param xoffset    Convenience parameter, added to every vertex
     * @param yoffset    Convenience parameter, added to every vertex
     * @param zoffset    Convenience parameter, added to every vertex
     * @return
     */
    public static DoubleBuffer getArcPoints(double startangle, double endangle, double radius, double xoffset, double yoffset, double zoffset) {
        // roughly 8 vertices per Minecraft 'pixel' - should result in at least 2 vertices per real pixel on the screen.
//        int numVertices = (int) Math.ceil(Math.abs((endangle - startangle) * 16 * Math.PI)); // getValue from wayyyyy back early on
        int numVertices = (int) Math.ceil(Math.abs((endangle - startangle) * 2 * Math.PI));
        double theta = (endangle - startangle) / numVertices;
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(numVertices * 3);

        double x = radius * Math.sin(startangle);
        double y = radius * Math.cos(startangle);
        double tf = Math.tan(theta); // precompute tangent factor: how much to move along the tangent line each iteration
        double rf = Math.cos(theta); // precompute radial factor: how much to move back towards the origin each iteration
        double tx;
        double ty;

        for (int i = 0; i < numVertices; i++) {
            buffer.put(x + xoffset);
            buffer.put(y + yoffset);
            buffer.put(zoffset);
            tx = y; // compute tangent lines
            ty = -x;
            x += tx * tf; // add tangent line * tangent factor
            y += ty * tf;
            x *= rf;
            y *= rf;
        }
        buffer.flip();
        return buffer;
    }

    /**
     * Creates a list of points linearly interpolated between points a and b noninclusive.
     *
     * @return A list of num points
     */
    public static List<Point2D> pointsInLine(int num, Point2D a, Point2D b) {
        List<Point2D> points = new ArrayList<>();
        switch (num) {
            case -1:
                break;
            case 0:
                break;
            case 1:
                points.add(b.minus(a).times(0.5F).plus(a));
                break;
            default:
                Point2D step = b.minus(a).times(1.0F / (num + 1));
                for (int i = 0; i < num; i++) {
                    points.add(a.plus(step.times(i + 1)));
                }
        }
        return points;
    }

    /**
     * Creates a list of points linearly interpolated between points a and b with a min spacing parameter for x and y.
     * Caution, this means that these points may extend beyond point b. This is useful in scrollable frames where a minimum
     * spacing is required in the case where multiple rendered objects are not desired to overlap
     *
     * @return A list of num points
     */
    public static List<Point2D> pointsInLine(int num, Point2D a, Point2D b, double minSpacingX, double minSpacingY) {
        List<Point2D> points = new ArrayList<>();
        switch (num) {
            case -1:
                break;
            case 0:
                break;
            case 1:
                // midpoint
                points.add(b.minus(a).times(0.5F).plus(a));
                break;
            default:
                // distance / num spaces
                Point2D step = b.minus(a).times(1.0F / (num + 1));
                if (step.getX() < 0) {
                    step.setX(Math.min(-minSpacingX, step.x));
                } else {
                    step.setX(Math.max(minSpacingX, step.x));
                }

                if (step.getY() < 0) {
                    step.setY(Math.min(-minSpacingY, step.y));
                } else {
                    step.setY(Math.max(minSpacingY, step.y));
                }

                for (int i = 0; i < num; i++) {
                    points.add(a.plus(step.times(i + 1)));
                }
        }
        return points;
    }

    /**
     * Returns a DoubleBuffer full of colours that are gradually interpolated
     *
     * @param c1
     * @param c2
     * @param numsegments
     * @return
     */
    public static DoubleBuffer getColourGradient(Colour c1, Colour c2, int numsegments) {
        DoubleBuffer buffer = BufferUtils.createDoubleBuffer(numsegments * 4);
        // declaring "i" as an int instead of double is what broke the swirly circle.
        for (double i = 0; i < numsegments; i++) {
            Colour c3 = c1.interpolate(c2, i / numsegments);
            buffer.put(c3.r);
            buffer.put(c3.g);
            buffer.put(c3.b);
            buffer.put(c3.a);
        }
        buffer.flip();
        return buffer;
    }
}