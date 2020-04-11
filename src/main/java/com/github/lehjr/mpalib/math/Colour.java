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

package com.github.lehjr.mpalib.math;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.mojang.blaze3d.systems.RenderSystem;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

/**
 * A class representing an RGBA colour and various helper functions. Mainly to
 * improve readability elsewhere.
 *
 * @author MachineMuse
 */
public class Colour {
    public static final Colour LIGHTBLUE = new Colour(0.5F, 0.5F, 1.0F, 1.0F);
    public static final Colour BLUE = new Colour(0.0F, 0.0F, 1.0F, 1.0F);
    public static final Colour DARKBLUE = new Colour(0.0F, 0.0F, 0.5F, 1.0F);
    public static final Colour ORANGE = new Colour(0.9F, 0.6F, 0.2F, 1.0F);
    public static final Colour YELLOW = new Colour(0.0F, 0.0F, 0.5F, 1.0F);
    public static final Colour WHITE = new Colour(1.0F, 1.0F, 1.0F, 1.0F);
    public static final Colour BLACK = new Colour(0.0F, 0.0F, 0.0F, 1.0F);
    public static final Colour LIGHTGREY = new Colour(0.827F,0.827F,0.827F,1.0F);
    public static final Colour DARKGREY = new Colour(0.4F, 0.4F, 0.4F, 1.0F);
    public static final Colour RED = new Colour(1.0F, 0.2F, 0.2F, 1.0F);
    public static final Colour LIGHTGREEN = new Colour(0.5F, 1.0F, 0.5F, 1.0F);
    public static final Colour GREEN = new Colour(0.0F, 1.0F, 0.0F, 1.0F);
    public static final Colour DARKGREEN = new Colour(0.0F, 0.8F, 0.2F, 1.0F);
    public static final Colour PURPLE = new Colour(0.6F, 0.1F, 0.9F, 1.0F);
    public static final Colour AQUA_BLUE = new Colour(0F, 1.0F, 1.0F, 1.0F);

    /**
     * The RGBA values are stored as floats from 0.0F (nothing) to 1.0F (full
     * saturation/opacity)
     */
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    /**
     * Constructor. Just sets the RGBA values to the parameters.
     */
    public Colour(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * Constructor. Just sets the RGBA values to the parameters.
     */
    public Colour(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1.0F;
    }

    public Colour(int r, int g, int b) {
        this.r = r / 255.0F;
        this.g = g / 255.0F;
        this.b = b / 255.0F;
        this.a = 1.0F;
    }

    /**
     * Takes colours in the integer format that Minecraft uses, and converts.
     */
    public Colour(int c) {
        this.a = (c >> 24 & 0xFF) / 255.0F;
        this.r = (c >> 16 & 0xFF) / 255.0F;
        this.g = (c >> 8 & 0xFF) / 255.0F;
        this.b = (c & 0xFF) / 255.0F;
    }

    public static int getInt(float r, float g, float b, float a) {
        int val = 0;
        val = val | ((int) (a * 255) << 24);
        val = val | ((int) (r * 255) << 16);
        val = val | ((int) (g * 255) << 8);
        val = val | ((int) (b * 255));

        return val;
    }

    public void doGL() {
        RenderSystem.color4f(r, g, b, a);
    }

    /**
     * Returns a colour with RGB set to the same getValue ie. a shade of grey.
     */
    public static Colour getGreyscale(float value, float alpha) {
        return new Colour(value, value, value, alpha);
    }

    public static void doGLByInt(int c) {
        float a = (c >> 24 & 255) / 255.0F;
        float r = (c >> 16 & 255) / 255.0F;
        float g = (c >> 8 & 255) / 255.0F;
        float b = (c & 255) / 255.0F;
        GL11.glColor4f(r, g, b, a);
    }

    /**
     * Handles RRGGBB and RRGGBBAA hex strings
     *
     * @param hexString
     * @return new colour based on getValue or default of white if error
     */
    public static Colour fromHexString(String hexString) {
        try {
            if (hexString == null || hexString.isEmpty())
                return WHITE;
            return new Colour((int) Long.parseLong(hexString, 16));

        } catch (Exception e) {
            MPALibLogger.logException("Failed to generate colour from Hex: ", e);
        }
        return WHITE;
    }

    /**
     * Returns this colour as an int in Minecraft's format (I think)
     * <p>
     * note: full values for RGBA will yield -1
     *
     * @return int getValue of this colour
     */
    public int getInt() {
        int val = 0;
        val = val | ((int) (a * 255) << 24);
        val = val | ((int) (r * 255) << 16);
        val = val | ((int) (g * 255) << 8);
        val = val | ((int) (b * 255));
        return val;
    }

    /**
     * Returns a colour at interval interval along a linear gradient from this
     * to target
     */
    public Colour interpolate(Colour target, float d) {
        float complement = 1 - d;
        return new Colour(this.r * complement + target.r * d, this.g * complement + target.g * d, this.b * complement + target.b * d, this.a
                * complement + target.a * d);
    }

    public Colour withAlpha(float newalpha) {
        return new Colour(this.r, this.g, this.b, newalpha);
    }

    public float[] asArray() {
        return new float[]{r, g, b, a};
    }

    // format is 0xRRGGBBAA
    public String hexColour() {
//        return hexDigits(r) + hexDigits(g) + hexDigits(b) + (a < 1 ? hexDigits(a) : "");
        return hexDigits(r) + hexDigits(g) + hexDigits(b) + (a > 0 ? hexDigits(a) : "");
//        return Integer.toHexString(getInt()).toUpperCase();
    }

    public String hexDigits(float x) {
        int y = (int) (x * 255);
        String hexDigits = "0123456789ABCDEF";
        return hexDigits.charAt(y / 16) + "" + hexDigits.charAt(y % 16);
    }

    @Override
    public String toString() {
        return "Colour{" +
                "r=" + r +
                ", g=" + g +
                ", b=" + b +
                ", a=" + a +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Colour colour = (Colour) o;
        return Double.compare(colour.r, r) == 0 &&
                Double.compare(colour.g, g) == 0 &&
                Double.compare(colour.b, b) == 0 &&
                Double.compare(colour.a, a) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, g, b, a);
    }
}