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
import com.github.lehjr.mpalib.math.Colour;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:01 AM, 30/04/13
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class GuiIcon {
    private final MPALibSpriteUploader spriteUploader;

    public final DrawableGuiIcon checkmark;
    public final DrawableGuiIcon transparentArmor;
    public final DrawableGuiIcon normalArmor;
    public final DrawableGuiIcon glowArmor;
    public final DrawableGuiIcon selectedArmorOverlay;
    public final DrawableGuiIcon armorColourPatch;
    public final DrawableGuiIcon minusSign;
    public final DrawableGuiIcon plusSign;
    public final DrawableGuiIcon glassTexture;

    public GuiIcon(MPALibSpriteUploader spriteUploader) {
        this.spriteUploader = spriteUploader;
        checkmark = registerSprite("checkmark", 16, 16);
        transparentArmor = registerSprite("transparentarmor", 8, 8);
        normalArmor = registerSprite("normalarmor", 8, 8);
        glowArmor= registerSprite("glowarmor", 8, 8);
        selectedArmorOverlay = registerSprite("armordisplayselect", 8, 8);
        armorColourPatch = registerSprite("colourclicker", 8, 8);
        minusSign = registerSprite("minussign", 8, 8);
        plusSign= registerSprite("plussign", 8, 8);
        glassTexture = registerSprite("glass", 1, 8);
    }

    private DrawableGuiIcon registerSprite(String name, int width, int height) {
        ResourceLocation location = new ResourceLocation(MPALIbConstants.MOD_ID, name);
        spriteUploader.registerSprite(location);
        return new DrawableGuiIcon(location, width, height);
    }

    public class DrawableGuiIcon {
        final ResourceLocation location;
        private final int width;
        private final int height;

        protected DrawableGuiIcon(ResourceLocation locationIn, int width, int height) {
            this.location = locationIn;
            this.width = width;
            this.height = height;
        }

        public void draw(float x, float y, Colour colour) {
            draw(x, y, 0, 0, 0, 0, colour);
        }

        public void draw(float xOffset, float yOffset, float maskTop, float maskBottom, float maskLeft, float maskRight, Colour colour) {
            float textureWidth = this.width;
            float textureHeight = this.height;

            Minecraft minecraft = Minecraft.getInstance();
            TextureManager textureManager = minecraft.getTextureManager();
            textureManager.bindTexture(MPALIbConstants.LOCATION_MPALIB_GUI_TEXTURE_ATLAS);
            TextureAtlasSprite icon = spriteUploader.getSprite(location);
            float zLevel = minecraft.currentScreen.getBlitOffset();

            float posLeft = xOffset + maskLeft;
            float posTop = yOffset + maskTop;
            float width = textureWidth - maskRight - maskLeft;
            float height = textureHeight - maskBottom - maskTop;

            float posRight = posLeft + width;
            float posBottom = posTop + height;

            float uSize = icon.getMaxU() - icon.getMinU();
            float vSize = icon.getMaxV() - icon.getMinV();
            float minU = icon.getMinU() + uSize * (maskLeft / textureWidth);
            float minV = icon.getMinV() + vSize * (maskTop / textureHeight);
            float maxU = icon.getMaxU() - uSize * (maskRight / textureWidth);
            float maxV = icon.getMaxV() - vSize * (maskBottom / textureHeight);

            RenderSystem.enableBlend();
            RenderSystem.disableAlphaTest();
            RenderSystem.defaultBlendFunc();
            innerBlit(posLeft, posRight, posTop, posBottom, zLevel, minU, maxU, minV, maxV, colour);
            RenderSystem.disableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableDepthTest();
        }

        public TextureAtlasSprite getSprite() {
            return spriteUploader.getSprite(location);
        }

        @Override
        public String toString() {
            Minecraft minecraft = Minecraft.getInstance();
            TextureManager textureManager = minecraft.getTextureManager();
            textureManager.bindTexture(MPALIbConstants.LOCATION_MPALIB_GUI_TEXTURE_ATLAS);
            TextureAtlasSprite icon = spriteUploader.getSprite(location);

            if (icon != null) {
                return "icon: " + icon.toString();
            } else {
                return "icon is null for location: " + location.toString();
            }
        }
    }

    /**
     *
     * @param location resource location of the Icon
     * @param left the left most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom bottom most position of the drawing rectangle
     * @param zLevel depth to draw at
     */
    public static void renderIcon8(ResourceLocation location, float left, float top, float right, float bottom, float zLevel) {
        renderIcon8(location, left, top, right, bottom, zLevel, Colour.WHITE);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param left the left most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom bottom most position of the drawing rectangle
     * @param zLevel depth to draw at
     * @param colour color to apply to the texture
     */
    public static void renderIcon8(ResourceLocation location, float left, float top, float right, float bottom, float zLevel, Colour colour) {
        renderTextureWithColour(location, left, right, top, bottom, zLevel, 8, 8, 0, 0, 8, 8, colour);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param left the left most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom bottom most position of the drawing rectangle
     * @param zLevel depth to draw at
     */
    public static void renderIcon16(ResourceLocation location, float left, float top, float right, float bottom, float zLevel) {
        renderIcon16(location, left, top, right, bottom, zLevel, Colour.WHITE);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param left the left most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom bottom most position of the drawing rectangle
     * @param zLevel depth to draw at
     * @param colour color to apply to the texture
     */
    public static void renderIcon16(ResourceLocation location, float left, float top, float right, float bottom, float zLevel, Colour colour) {
        renderTextureWithColour(location, left, right, top, bottom, zLevel, 16, 16, 0, 0, 16, 16, colour);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param left the left most position of the drawing rectangle
     * @param right the right most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom the bottom most position of the drawing rectangle
     * @param zLevel depth at which to draw (usually Minecraft.getInstance().currentScreen.getBlitLevel())
     * @param iconWidth actual width of the icon to draw
     * @param iconHeight actual height of the icon to draw
     * @param texStartX the leftmost point of the texture on the sheet (usually 0 for an icon)
     * @param texStartY the topmost point of the texture on the sheet (usually 0 for an icon)
     * @param textureWidth the width of the texture (often 8 or 16 for icons)
     * @param textureHeight the height of the texture (often 8 or 16 for icons)
     * @param colour the Colour to apply to the texture
     */
    public static void renderTextureWithColour(ResourceLocation location,
                                               float left, float right, float top, float bottom, float zLevel, float iconWidth, float iconHeight, float texStartX, float texStartY, float textureWidth, float textureHeight, Colour colour) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(location);
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        innerBlit(left, right, top, bottom, zLevel, iconWidth, iconHeight, texStartX, texStartY, textureWidth, textureHeight, colour);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableDepthTest();
    }

    /**
     *
     * @param left the left most position of the drawing rectangle
     * @param right the right most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom the bottom most position of the drawing rectangle
     * @param zLevel depth to render at
     * @param iconWidth width of the portion of the texture to display
     * @param iconHeight iconHeight of the portion of texture to display
     * @param texStartX location of the left of the texture on the sheet
     * @param texStartY location of the top of the texture on the sheet
     * @param textureWidth total texture sheet width
     * @param textureHeight total texture sheet iconHeight
     * @param colour colour to apply to the texture
     */
    private static void innerBlit(float left, float right, float top, float bottom, float zLevel, float iconWidth, float iconHeight, float texStartX, float texStartY, float textureWidth, float textureHeight, Colour colour) {
        innerBlit(left, right, top, bottom, zLevel,
                (texStartX + 0.0F) / textureWidth,
                (texStartX + iconWidth) / textureWidth,
                (texStartY + 0.0F) / textureHeight,
                (texStartY + iconHeight) / textureHeight,
                colour);
    }

    /**
     * Basically like vanilla's version but with floats and a colour parameter
     * Only does the inner texture rendering
     *
     * @param left the left most position of the drawing rectangle
     * @param right the right most position of the drawing rectangle
     * @param top the top most position of the drawing rectangle
     * @param bottom the bottom most position of the drawing rectangle
     * @param zLevel the depth position of the drawing rectangle
     * Note: UV positions are scaled (0.0 - 1.0)
     * @param minU the left most UV mapped position
     * @param maxU the right most UV mapped position
     * @param minV the top most UV mapped position
     * @param maxV the bottom most UV mapped position
     * @param colour the Colour to apply to the texture
     */
    public static void innerBlit(float left, float right, float top, float bottom, float zLevel, float minU, float maxU, float minV, float maxV, Colour colour) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();

        colour.doGL();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        // bottom left
        bufferBuilder.pos(left, bottom, zLevel).tex(minU, maxV).endVertex();
        // bottom right
        bufferBuilder.pos(right, bottom, zLevel).tex(maxU, maxV).endVertex();
        // top right
        bufferBuilder.pos(right, top, zLevel).tex(maxU, minV).endVertex();
        // top left
        bufferBuilder.pos(left, top, zLevel).tex(minU, minV).endVertex();

        tessellator.draw();
    }
}