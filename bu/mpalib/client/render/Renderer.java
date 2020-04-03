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

package com.github.lehjr.mpalib.client.render;

import com.github.lehjr.mpalib.client.gui.clickable.IClickable;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.geometry.SwirlyCircle;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.List;

import static com.github.lehjr.mpalib.basemod.MPALIbConstants.LIGHTNING_TEXTURE;

/**
 * Contains a bunch of random OpenGL-related functions, accessed statically.
 *
 * @author MachineMuse
 */
public abstract class Renderer {

    protected static ItemRenderer renderItem;

    protected static SwirlyCircle selectionCircle;
    static boolean messagedAboutSlick = false;

    /**
     * Does the rotating green circle around the selection, e.g. in GUI.
     *
     * @param xoffset
     * @param yoffset
     * @param radius
     */
    public static void drawCircleAround(double xoffset, double yoffset, double radius) {
        if (selectionCircle == null)
            selectionCircle = new SwirlyCircle(new Colour(0.0f, 1.0f, 0.0f, 0.0f), new Colour(0.8f, 1.0f, 0.8f, 1.0f));
        selectionCircle.draw(radius, xoffset, yoffset);
    }


    // FIXME: need lighting/shading for this module model


    public static void drawModuleAt(double x, double y, @Nonnull ItemStack itemStack, boolean active) {
        drawItemAt(x, y, itemStack);
    }

    /**
     * Makes the appropriate openGL calls and draws an itemStack and overlay using the default icon
     */
    public static void drawItemAt(double x, double y, @Nonnull ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            RenderState.on2D();
            Minecraft.getInstance().getItemRenderer().renderItemAndEffectIntoGUI(itemStack, (int) x, (int) y);
            Minecraft.getInstance().getItemRenderer().renderItemOverlayIntoGUI(getFontRenderer(), itemStack, (int) x, (int) y, (String) null);
            RenderState.off2D();
        }
    }

    public static void drawString(String s, double x, double y) {
        drawString(s, x, y, Colour.WHITE);
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string at the specified coords
     */
    public static void drawString(String s, double x, double y, Colour c) {
        getFontRenderer().drawStringWithShadow(s, (int) x, (int) y, c.getInt());
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string such that the xcoord is halfway through the string
     */
    public static void drawCenteredString(String s, double x, double y) {
        drawString(s, x - getStringWidth(s) / 2, y);
    }

    /**
     * Does the necessary openGL calls and calls the Minecraft font renderer to draw a string such that the xcoord is halfway through the string
     */
    public static void drawRightAlignedString(String s, double x, double y) {
        drawString(s, x - getStringWidth(s), y);
    }

    public static void drawLeftAlignedStringString(String s, double x, double y) {
        drawString(s, x, y);
    }


    public static double getStringWidth(String s) {
        double stringWidth;
        GL11.glPushAttrib(GL11.GL_TEXTURE_BIT);
        stringWidth = getFontRenderer().getStringWidth(s);
        GL11.glPopAttrib();
        return stringWidth;
    }

    public static void drawStringsJustified(List<String> words, double x1, double x2, double y) {
        int totalwidth = 0;
        for (String word : words) {
            totalwidth += getStringWidth(word);
        }

        double spacing = (x2 - x1 - totalwidth) / (words.size() - 1);

        double currentwidth = 0;
        for (String word : words) {
            Renderer.drawString(word, x1 + currentwidth, y);
            currentwidth += getStringWidth(word) + spacing;
        }
    }

    /**
     * Singleton pattern for FontRenderer
     */
    public static FontRenderer getFontRenderer() {
        return Minecraft.getInstance().fontRenderer;
    }

    /**
     * Singleton pattern for RenderEngine
     */
    public static TextureManager getRenderEngine() {
        return Minecraft.getInstance().textureManager;
    }

    /**
     * Singleton pattern for the RenderItem
     *
     * @return the static renderItem instance
     */
    public static ItemRenderer getRenderItem(TextureManager textureManager, ModelManager modelManager) {
        if (renderItem == null) {
            renderItem = new ItemRenderer(textureManager, modelManager, null); // FIXME!!!! this shoudl be an itemStack color but might be good enough to get running
        }
        return renderItem;
    }

    public static void drawLineBetween(IClickable firstClickable, IClickable secondClickable, Colour gradientColour) {
        long varia = System.currentTimeMillis() % 2000 - 1000; // ranges from
        // -1000 to 1000
        // and around,
        // period = 2
        // seconds
        double gradientRatio = 1.0 - ((varia + 1000) % 1000) / 1000.0;
        Point2D midpoint = (firstClickable.getPosition().minus(secondClickable.getPosition()).times(Math.abs(varia / 1000.0))
                .plus(secondClickable.getPosition()));
        Point2D firstpoint, secondpoint;
        if (varia < 0) {
            firstpoint = secondClickable.getPosition();
            secondpoint = firstClickable.getPosition();
        } else {
            firstpoint = firstClickable.getPosition();
            secondpoint = secondClickable.getPosition();
        }
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBegin(GL11.GL_LINES);
        gradientColour.withAlpha(gradientRatio).doGL();
        GL11.glVertex3d(midpoint.getX(), midpoint.getY(), 1);
        gradientColour.withAlpha(0.0).doGL();
        GL11.glVertex3d(firstpoint.getX(), firstpoint.getY(), 1);

        gradientColour.withAlpha(gradientRatio).doGL();
        GL11.glVertex3d(secondpoint.getX(), secondpoint.getY(), 1);
        Colour.WHITE.withAlpha(1.0).doGL();
        GL11.glVertex3d(midpoint.getX(), midpoint.getY(), 1);
        GL11.glEnd();
        GL11.glPopAttrib();
    }

    public static void drawLightning(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        drawLightningTextured(x1, y1, z1, x2, y2, z2, colour);
    }

    public static void drawMPDLightning(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour, double displacement,
                                        double detail) {
        if (displacement < detail) {
            colour.doGL();
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x2, y2, z2);
            GL11.glEnd();
        } else {
            double mid_x = (x1 + x2) / 2.0;
            double mid_y = (y1 + y2) / 2.0;
            double mid_z = (z1 + z2) / 2.0;
            mid_x += (Math.random() - 0.5) * displacement;
            mid_y += (Math.random() - 0.5) * displacement;
            mid_z += (Math.random() - 0.5) * displacement;
            drawMPDLightning(x1, y1, z1, mid_x, mid_y, mid_z, colour, displacement / 2, detail);
            drawMPDLightning(mid_x, mid_y, mid_z, x2, y2, z2, colour, displacement / 2, detail);
        }
    }

    public static void drawLightningTextured(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        double tx = x2 - x1, ty = y2 - y1, tz = z2 - z1;

        double ax, ay, az;
        double bx, by, bz;
        double cx = 0, cy = 0, cz = 0;

        double jagfactor = 0.3;
        RenderState.on2D();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        TextureUtils.pushTexture(LIGHTNING_TEXTURE);
        RenderState.blendingOn();
        colour.doGL();
        GL11.glBegin(GL11.GL_QUADS);
        while (Math.abs(cx) < Math.abs(tx) && Math.abs(cy) < Math.abs(ty) && Math.abs(cz) < Math.abs(tz)) {
            ax = x1 + cx;
            ay = y1 + cy;
            az = z1 + cz;
            cx += Math.random() * tx * jagfactor - 0.1 * tx;
            cy += Math.random() * ty * jagfactor - 0.1 * ty;
            cz += Math.random() * tz * jagfactor - 0.1 * tz;
            bx = x1 + cx;
            by = y1 + cy;
            bz = z1 + cz;

            int index = (int) Math.random() * 50; // FIXME: Math.random() cast to int is always rounded down to 0

            drawLightningBetweenPointsFast(ax, ay, az, bx, by, bz, index);
        }
        GL11.glEnd();
        RenderState.blendingOff();
        RenderState.off2D();
    }

    public static void drawLightningBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2, int index) {
        TextureUtils.pushTexture(LIGHTNING_TEXTURE);
        double u1 = index / 50.0;
        double u2 = u1 + 0.02;
        double px = (y1 - y2) * 0.125;
        double py = (x2 - x1) * 0.125;
        GL11.glTexCoord2d(u1, 0);
        GL11.glVertex3d(x1 - px, y1 - py, z1);
        GL11.glTexCoord2d(u2, 0);
        GL11.glVertex3d(x1 + px, y1 + py, z1);
        GL11.glTexCoord2d(u1, 1);
        GL11.glVertex3d(x2 - px, y2 - py, z2);
        GL11.glTexCoord2d(u2, 1);
        GL11.glVertex3d(x2 + px, y2 + py, z2);
        TextureUtils.popTexture();
    }

    public static void drawLightningBetweenPointsFast(double x1, double y1, double z1, double x2, double y2, double z2, int index) {

        double u1 = index / 50.0;
        double u2 = u1 + 0.02;
        double px = (y1 - y2) * 0.125;
        double py = (x2 - x1) * 0.125;
        GL11.glTexCoord2d(u1, 0);
        GL11.glVertex3d(x1 - px, y1 - py, z1);
        GL11.glTexCoord2d(u2, 0);
        GL11.glVertex3d(x1 + px, y1 + py, z1);
        GL11.glTexCoord2d(u1, 1);
        GL11.glVertex3d(x2 - px, y2 - py, z2);
        GL11.glTexCoord2d(u2, 1);
        GL11.glVertex3d(x2 + px, y2 + py, z2);
    }

    public static void drawLightningLines(double x1, double y1, double z1, double x2, double y2, double z2, Colour colour) {
        double tx = x2 - x1, ty = y2 - y1, tz = z2 - z1, cx = 0, cy = 0, cz = 0;
        double jagfactor = 0.3;
        RenderState.texturelessOn();
        RenderState.blendingOn();
        RenderState.on2D();
        GL11.glBegin(GL11.GL_LINE_STRIP);
        while (Math.abs(cx) < Math.abs(tx) && Math.abs(cy) < Math.abs(ty) && Math.abs(cz) < Math.abs(tz)) {
            colour.doGL();
            // GL11.glLineWidth(1);
            cx += Math.random() * tx * jagfactor - 0.1 * tx;
            cy += Math.random() * ty * jagfactor - 0.1 * ty;
            cz += Math.random() * tz * jagfactor - 0.1 * tz;
            GL11.glVertex3d(x1 + cx, y1 + cy, z1 + cz);
            //
            // GL11.glLineWidth(3);
            // colour.withAlpha(0.5).doGL();
            // GL11.glVertex3d(ox, oy, oz);
            //
            // GL11.glLineWidth(5);
            // colour.withAlpha(0.1).doGL();
            // GL11.glVertex3d(x1 + cx, y1 + cy, z1 + cz);
        }
        GL11.glEnd();
        RenderState.off2D();
        RenderState.blendingOff();
        RenderState.texturelessOff();
    }

    public static void unRotate() {
        BillboardHelper.unRotate();
    }
}