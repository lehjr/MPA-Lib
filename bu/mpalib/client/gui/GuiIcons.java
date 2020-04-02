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

package com.github.lehjr.mpalib.client.gui;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.client.render.IconUtils;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.client.render.TextureUtils;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 AM, 30/04/13
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class GuiIcons {
    public static class GuiIcon {
        final float size;
        final ResourceLocation location;

        final float x;
        final float y;
        final Colour c;
        final float xmin;
        final float ymin;
        final float xmax;
        final float ymax;

        public GuiIcon(float size, String locationIn, float x, float y, Colour c, Float xmin, Float ymin, Float xmax, Float ymax) {
            this(size, new ResourceLocation(locationIn), x, y, c, xmin, ymin, xmax, ymax);
        }

        public GuiIcon(float size, ResourceLocation locationIn, float x, float y, Colour c, Float xmin, Float ymin, Float xmax, Float ymax) {
            this.size = size;
            this.location = locationIn.getPath().toLowerCase().endsWith(".png") ? locationIn : new ResourceLocation(locationIn.getNamespace(), locationIn.getPath() + ".png");
            this.x = x;
            this.y = y;
            this.c = (c != null) ? c : Colour.WHITE;
            this.xmin = (xmin != null) ? xmin : Integer.MIN_VALUE;
            this.ymin = (ymin != null) ? ymin : Integer.MIN_VALUE;
            this.xmax = (xmax != null) ? xmax : Integer.MAX_VALUE;
            this.ymax = (ymax != null) ? ymax : Integer.MAX_VALUE;

            TextureUtils.pushTexture(location);
            GL11.glPushMatrix();
            float s = size / 16.0F;
            RenderState.blendingOn();
            GL11.glScaled(s, s, s);

            IconUtils.drawIconPartialOccluded(x / s, y / s, new GuiIconDrawer(location) /* FIXME: this will need to be changed to a reference or id */, this.c, this.xmin / s, this.ymin / s, this.xmax / s, this.ymax / s);
            RenderState.blendingOff();
            GL11.glPopMatrix();
            TextureUtils.popTexture();
        }
    }

    public static class Checkmark extends GuiIcon {
        public Checkmark(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(16.0F, MPALIbConstants.TEXTURE_PREFIX + "gui/checkmark.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class TransparentArmor extends GuiIcon {
        public TransparentArmor(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/transparentarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class NormalArmor extends GuiIcon {
        public NormalArmor(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/normalarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class GlowArmor extends GuiIcon {
        public GlowArmor(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/glowarmor.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class SelectedArmorOverlay extends GuiIcon {
        public SelectedArmorOverlay(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/armordisplayselect.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class ArmourColourPatch extends GuiIcon {
        public ArmourColourPatch(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/colourclicker.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class MinusSign extends GuiIcon {
        public MinusSign(float x, float y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/minussign.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class PlusSign extends GuiIcon {
        public PlusSign(float x, double y, Colour c, Double xmin, Double ymin, Double xmax, Double ymax) {
            super(8.0, MPALIbConstants.TEXTURE_PREFIX + "gui/plussign.png", x, y, c, xmin, ymin, xmax, ymax);
        }
    }

    public static class GuiIconDrawer extends TextureAtlasSprite {
        protected GuiIconDrawer(ResourceLocation locationIn) {
            super(locationIn, 8, 8);
        }

        protected GuiIconDrawer(String locationIn) {
            super(new ResourceLocation(locationIn), 8, 8);
        }

        @Override
        public float getMinU() {
            return 0;
        }

        @Override
        public float getMaxU() {
            return 1;
        }

        @Override
        public float getInterpolatedU(double d0) {
            return (float) d0;
        }

        @Override
        public float getMinV() {
            return 0;
        }

        @Override
        public float getMaxV() {
            return 1;
        }

        @Override
        public float getInterpolatedV(double d0) {
            return (float) d0;
        }

        public int getOriginX() {
            return 0;
        }

        public int getOriginY() {
            return 0;
        }
    }
}