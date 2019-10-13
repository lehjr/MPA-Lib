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

package com.github.lehjr.mpalib.client.render;

import com.mojang.blaze3d.platform.GLX;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import org.lwjgl.opengl.GL11;

import java.nio.DoubleBuffer;

import static org.lwjgl.system.MemoryUtil.memAddress;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:41 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/23/16.
 */
public final class RenderState {
    /**
     * Used primarily for model rendering to make a surface "glow"
     */
    private static float lightmapLastX = .0f;
    private static float lightmapLastY = .0f;

    /**
     * 2D rendering mode on/off
     */
    public static void on2D() {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public static void off2D() {
        GL11.glPopAttrib();
    }

    /**
     * Arrays on/off
     */
    public static void arraysOnColor() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
    }

    public static void arraysOnTexture() {
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    public static void arraysOff() {
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
    }

    /**
     * Call before doing any pure geometry (ie. with colours rather than textures).
     */
    public static void texturelessOn() {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Call after doing pure geometry (ie. with colours) to go back to the texture mode (default).
     */
    public static void texturelessOff() {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    /**
     * Call before doing anything with alpha blending
     */
    public static void blendingOn() {
        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
        if (Minecraft.isFancyGraphicsEnabled()) {
            GL11.glShadeModel(GL11.GL_SMOOTH);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * Call after doing anything with alpha blending
     */
    public static void blendingOff() {
        GL11.glPopAttrib();
    }

    public static void glColorPointer(int size, int stride, DoubleBuffer pointer) {
        GL11.nglColorPointer(size, GL11.GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void glVertexPointer(int size, int stride, DoubleBuffer pointer) {
        GL11.nglVertexPointer(size, GL11.GL_DOUBLE, stride, memAddress(pointer));
    }

    public static void scissorsOn(double x, double y, double w, double h) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_SCISSOR_BIT);
        GL11.glPushMatrix();
        Minecraft mc = Minecraft.getInstance();
//        int dw = mc.mainWindow.getWidth();
        int dh = mc.mainWindow.getHeight();
        double scaleFactor = mc.mainWindow.getGuiScaleFactor();
        double newx = x * scaleFactor;
        double newy = dh - h * scaleFactor - y * scaleFactor;
        double neww = w * scaleFactor;
        double newh = h * scaleFactor;
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) newx, (int) newy, (int) neww, (int) newh);
    }

    public static void scissorsOff() {
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public static void glowOn() {
        GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
        lightmapLastX = GLX.lastBrightnessX;
        lightmapLastY = GLX.lastBrightnessY;
        RenderHelper.disableStandardItemLighting();
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, 240.0F, 240.0F);
    }

    public static void glowOff() {
        GLX.glMultiTexCoord2f(GLX.GL_TEXTURE1, lightmapLastX, lightmapLastY);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopAttrib();
    }
}