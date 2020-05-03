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
            float uSize = icon.getMaxU() - icon.getMinU();
            float vSize = icon.getMaxV() - icon.getMinV();

            float minU = icon.getMinU() + uSize * (maskLeft / textureWidth);
            float minV = icon.getMinV() + vSize * (maskTop / textureHeight);
            float maxU = icon.getMaxU() - uSize * (maskRight / textureWidth);
            float maxV = icon.getMaxV() - vSize * (maskBottom / textureHeight);

//            RenderSystem.enableBlend();
//            RenderSystem.disableAlphaTest();
//            RenderSystem.defaultBlendFunc();
//            RenderSystem.shadeModel(GL11.GL_SMOOTH);


            RenderSystem.disableDepthTest();


//            colour.doGL();

//            Tessellator tessellator = Tessellator.getInstance();

            innerBlit(posLeft, posLeft + width, posTop, posTop + height, zLevel, minU, maxU, minV, maxV, colour);

//            BufferBuilder bufferBuilder = tessellator.getBuffer();
//            bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
//
//            // bottom left
//            bufferBuilder.pos(posLeft, posTop + height, zLevel)
//                    .tex(minU, maxV).endVertex();
//
//            // bottom right
//            bufferBuilder.pos(posLeft + width, posTop + height, zLevel)
//                    .tex(maxU, maxV).endVertex();
//
//            // top right
//            bufferBuilder.pos(posLeft + width, posTop, zLevel)
//                    .tex(maxU, minV).endVertex();
//
//            // top left
//            bufferBuilder.pos(posLeft, posTop, zLevel)
//                    .tex(minU, minV) .endVertex();
//            tessellator.draw();

//            RenderSystem.shadeModel(GL11.GL_FLAT);
//            RenderSystem.disableBlend();
//            RenderSystem.enableAlphaTest();

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
     * Renders an 8x8 stand alone icon
     *
     * @param location resource location of the Icon
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public static void renderIcon8(ResourceLocation location, float posLeft, float posTop, float width, float height) {
        renderTexture(location, posLeft, posTop, width, height, 0, 0, 8, 8);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param colour the Colour to apply to the texture
     */
    public static void renderIcon8(ResourceLocation location, float posLeft, float posTop, float width, float height, Colour colour) {
        renderTextureWithColour(location, posLeft, posTop, width, height, 0, 0, 8, 8, colour);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     */
    public static void renderIcon16(ResourceLocation location, float posLeft, float posTop, float width, float height) {
        renderTexture(location, posLeft, posTop, width, height, 0, 0, 16, 16);
    }

    /**
     *
     * @param location resource location of the Icon
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param colour the Colour to apply to the texture
     */
    public static void renderIcon16(ResourceLocation location, float posLeft, float posTop, float width, float height, Colour colour) {
        renderTextureWithColour(location, posLeft, posTop, width, height, 0, 0, 16, 16, colour);
    }

    /**
     * Actually render the texture
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param textStartX the leftmost point of the texture on the sheet (usually 0 for an icon)
     * @param textStartY the topmost point of the texture on the sheet (usually 0 for an icon)
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param textureWidth the width of the texture (often 8 or 16 for icons)
     * @param textureHeight the height of the texture (often 8 or 16 for icons)
     */
    public static void renderTexture(ResourceLocation location, float posLeft, float posTop, float width, float height, float textStartX, float textStartY, float textureWidth, float textureHeight) {
        renderTextureWithColour(location, posLeft, posTop, width, height, textStartX, textStartY, textureWidth, textureHeight, Colour.WHITE);
    }

    /**
     * Actually render the texture
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param textStartX the leftmost point of the texture on the sheet (usually 0 for an icon)
     * @param textStartY the topmost point of the texture on the sheet (usually 0 for an icon)
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param textureWidth the width of the texture (often 8 or 16 for icons)
     * @param textureHeight the height of the texture (often 8 or 16 for icons)
     * @param colour the Colour to apply to the texture
     */
    public static void renderTextureWithColour(ResourceLocation location, float posLeft, float posTop, float width, float height, float textStartX, float textStartY, float textureWidth, float textureHeight, Colour colour) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(location);
        float zLevel = minecraft.currentScreen.getBlitOffset();
        RenderSystem.disableDepthTest();
        blit(posLeft, posTop, zLevel, textStartX, textStartY, width, height,  textureWidth, textureHeight, colour);
        RenderSystem.enableDepthTest();
    }

    /**
     * Basically like vanilla's version but with floats and a colour parameter
     * Only does the inner texture rendering
     *
     * @param posLeft the left most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param zLevel the depth position of the drawing rectangle
     * @param textStartX the leftmost point of the texture on the sheet (usually 0 for an icon)
     * @param textStartY the topmost point of the texture on the sheet (usually 0 for an icon)
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param textureWidth the width of the texture (often 8 or 16 for icons)
     * @param textureHeight the height of the texture (often 8 or 16 for icons)
     * @param colour the Colour to apply to the texture
     */
    public static void blit(float posLeft, float posTop, float zLevel, float textStartX, float textStartY, float width, float height, float textureWidth, float textureHeight, Colour colour) {
        blit(posLeft, posLeft + width, posTop, posTop + height, zLevel, width, height, textStartX, textStartY, textureHeight, textureWidth, colour);
    }

    /**
     * Basically like vanilla's version but with floats and a colour parameter
     * Only does the inner texture rendering
     *
     * @param posLeft the left most position of the drawing rectangle
     * @param posRight the right most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param posBottom the bottom most position of the drawing rectangle
     * @param zLevel the depth position of the drawing rectangle
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param textStartX the leftmost point of the texture on the sheet (usually 0 for an icon)
     * @param textStartY the topmost point of the texture on the sheet (usually 0 for an icon)
     * @param textureWidth the width of the texture (often 8 or 16 for icons)
     * @param textureHeight the height of the texture (often 8 or 16 for icons)
     * @param colour the Colour to apply to the texture
     */
    public static void blit(float posLeft, float posRight, float posTop, float posBottom, float zLevel, float width, float height, float textStartX, float textStartY, float textureWidth, float textureHeight, Colour colour) {
        innerBlit(posLeft, posRight, posTop, posBottom, zLevel, textStartX / textureWidth, (textStartX + width) / textureWidth, textStartY / textureHeight, (textStartY + height) / textureHeight, colour);
    }

    /**
     * Basically like vanilla's version but with floats and a colour parameter
     * Only does the inner texture rendering
     *
     * @param posLeft the left most position of the drawing rectangle
     * @param posRight the right most position of the drawing rectangle
     * @param posTop the top most position of the drawing rectangle
     * @param posBottom the bottom most position of the drawing rectangle
     * @param zLevel the depth position of the drawing rectangle
     * Note: UV positions are scaled (0.0 - 1.0)
     * @param minU the left most UV mapped position
     * @param maxU the right most UV mapped position
     * @param minV the top most UV mapped position
     * @param maxV the bottom most UV mapped position
     * @param colour the Colour to apply to the texture
     */
    public static void innerBlit(float posLeft, float posRight, float posTop, float posBottom, float zLevel, float minU, float maxU, float minV, float maxV, Colour colour) {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        if (colour != Colour.WHITE) {
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR_TEX);
            // bottom left
            bufferbuilder.pos(posLeft, posBottom, zLevel).color(colour.r, colour.b, colour.g, colour.a).tex(minU, maxV).endVertex();
            // bottom right
            bufferbuilder.pos(posRight, posBottom, zLevel).color(colour.r, colour.b, colour.g, colour.a).tex(maxU, maxV).endVertex();
            // top right
            bufferbuilder.pos(posRight, posTop, zLevel).color(colour.r, colour.b, colour.g, colour.a).tex(maxU, minV).endVertex();
            // top left
            bufferbuilder.pos(posLeft, posTop, zLevel).color(colour.r, colour.b, colour.g, colour.a).tex(minU, minV).endVertex();
        } else {
            bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            // bottom left
            bufferbuilder.pos(posLeft, posBottom, zLevel).tex(minU, maxV).endVertex();
            // bottom right
            bufferbuilder.pos(posRight, posBottom, zLevel).tex(maxU, maxV).endVertex();
            // top right
            bufferbuilder.pos(posRight, posTop, zLevel).tex(maxU, minV).endVertex();
            // top left
            bufferbuilder.pos(posLeft, posTop, zLevel).tex(minU, minV).endVertex();
        }
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }
}