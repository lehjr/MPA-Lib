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

package com.github.lehjr.mpalib.client.gui.frame;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Matrix4f;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.realms.Tezzelator;
import net.minecraft.util.text.ITextComponent;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ScrollableFrame implements IGuiFrame {
    protected final int buttonsize = 5;
    protected int totalsize;
    protected int currentscrollpixels;
    protected boolean visible = true;
    protected boolean enabled = true;

    protected DrawableRect border;

    public ScrollableFrame(Point2D topleft, Point2D bottomright, Colour backgroundColour, Colour borderColour) {
        border = new DrawableRect(topleft, bottomright, backgroundColour, borderColour);
    }

    @Override
    public IRect getBorder() {
        return border;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible ;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            preRender(mouseX, mouseY, partialTicks);
            postRender(mouseX, mouseY, partialTicks);
        }
    }

    public void preRender(int mouseX, int mouseY, float partialTicks)  {
        if (isVisible()) {
            border.draw();
            RenderState.glowOn();
            RenderState.texturelessOn();
            GlStateManager.begin(GL11.GL_TRIANGLES);
            Colour.LIGHTBLUE.doGL();
            // Can scroll down
            if (currentscrollpixels + border.height() < totalsize) {
                GL11.glVertex3d(border.left() + border.width() / 2, border.bottom(), 1);
                GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.bottom() - 4, 1);
                GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.bottom() - 4, 1);
            }
            // Can scroll up
            if (currentscrollpixels > 0) {
                GL11.glVertex3d(border.left() + border.width() / 2, border.top(), 1);
                GL11.glVertex3d(border.left() + border.width() / 2 - 2, border.top() + 4, 1);
                GL11.glVertex3d(border.left() + border.width() / 2 + 2, border.top() + 4, 1);
            }
            Colour.WHITE.doGL();
            GlStateManager.end();
            RenderState.texturelessOff();
            RenderState.scissorsOn(border.left(), border.top() + 4, border.width(), border.height() - 8); // get rid of margins
        }
    }
/*
    private int blitOffset;

    void setBlitOffset(int offset) {
        this.blitOffset = offset;
    }

    public void renderTooltip(List<String> stringList, int p_renderTooltip_2_, int p_renderTooltip_3_, FontRenderer font) {
        net.minecraftforge.fml.client.gui.GuiUtils.drawHoveringText(stringList, p_renderTooltip_2_, p_renderTooltip_3_, width, height, -1, font);
        if (false && !stringList.isEmpty()) {
            RenderSystem.disableRescaleNormal();
            RenderSystem.disableDepthTest();
            int boxWidth = 0;

            for(String s : stringList) {
                int j = font.getStringWidth(s);
                if (j > boxWidth) {
                    boxWidth = j;
                }
            }

            int l1 = p_renderTooltip_2_ + 12;
            int i2 = p_renderTooltip_3_ - 12;
            int boxHeight = 8;
            if (stringList.size() > 1) {
                boxHeight += 2 + (stringList.size() - 1) * 10;
            }

            if (l1 + boxWidth > this.width) {
                l1 -= 28 + boxWidth;
            }

            if (i2 + boxHeight + 6 > this.height) {
                i2 = this.height - boxHeight - 6;
            }

            this.setBlitOffset(300);
            this.itemRenderer.zLevel = 300.0F;
            int color1 = -267386864;
            this.fillGradient(l1 - 3, i2 - 4, l1 + boxWidth + 3, i2 - 3, color1, color1);
            this.fillGradient(l1 - 3, i2 + boxHeight + 3, l1 + boxWidth + 3, i2 + boxHeight + 4, color1, color1);
            this.fillGradient(l1 - 3, i2 - 3, l1 + boxWidth + 3, i2 + boxHeight + 3, color1, color1);
            this.fillGradient(l1 - 4, i2 - 3, l1 - 3, i2 + boxHeight + 3, color1, color1);
            this.fillGradient(l1 + boxWidth + 3, i2 - 3, l1 + boxWidth + 4, i2 + boxHeight + 3, color1, color1);

            int color2 = 1347420415;
            int color3 = 1344798847;
            this.fillGradient(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + boxHeight + 3 - 1, color2, color3);
            this.fillGradient(l1 + boxWidth + 2, i2 - 3 + 1, l1 + boxWidth + 3, i2 + boxHeight + 3 - 1, color2, color3);
            this.fillGradient(l1 - 3, i2 - 3, l1 + boxWidth + 3, i2 - 3 + 1, color2, color2);
            this.fillGradient(l1 - 3, i2 + boxHeight + 2, l1 + boxWidth + 3, i2 + boxHeight + 3, color3, color3);

            MatrixStack matrixstack = new MatrixStack();
            IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
            matrixstack.translate(0.0D, 0.0D, (double)this.itemRenderer.zLevel);
            Matrix4f matrix4f = matrixstack.getLast().getMatrix();

            for(int k1 = 0; k1 < stringList.size(); ++k1) {
                String s1 = stringList.get(k1);
                if (s1 != null) {
                    font.renderString(s1, (float)l1, (float)i2, -1, true, matrix4f, irendertypebuffer$impl, false, 0, 15728880);
                }

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            irendertypebuffer$impl.finish();
            this.setBlitOffset(0);
            this.itemRenderer.zLevel = 0.0F;
            RenderSystem.enableDepthTest();
            RenderSystem.enableRescaleNormal();
        }
    }


    protected void fillGradient(int right, int top, int left, int bottom, int colorInt1, int colorInt2) {
        float alpha1 = (float)(colorInt1 >> 24 & 255) / 255.0F;
        float red1 = (float)(colorInt1 >> 16 & 255) / 255.0F;
        float green1 = (float)(colorInt1 >> 8 & 255) / 255.0F;
        float blue1 = (float)(colorInt1 & 255) / 255.0F;
        float alpha2 = (float)(colorInt2 >> 24 & 255) / 255.0F;
        float red2 = (float)(colorInt2 >> 16 & 255) / 255.0F;
        float green2 = (float)(colorInt2 >> 8 & 255) / 255.0F;
        float blue2 = (float)(colorInt2 & 255) / 255.0F;
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(7425); // "GL11.GL_SMOOTH"
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        //            pos(double x, double y, double z)
        bufferbuilder.pos(left, top, this.blitOffset).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(right, top, this.blitOffset).color(red1, green1, blue1, alpha1).endVertex();
        bufferbuilder.pos(right, bottom, this.blitOffset).color(red2, green2, blue2, alpha2).endVertex();
        bufferbuilder.pos(left, bottom, this.blitOffset).color(red2, green2, blue2, alpha2).endVertex();

        tessellator.draw();
        RenderSystem.shadeModel(7424); // "GL11.GL_FLAT"
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }
*/

    public void postRender(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            RenderState.scissorsOff();
            RenderState.glowOff();
        }
    }

    public int getMaxScrollPixels() {
        return (int) Math.max(totalsize - border.height(), 0);
    }

    protected double getScrollAmount() {
        return 8;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dWheel) {
        if (border.containsPoint(mouseX, mouseY)) {
            // prevent negative total scroll values
            currentscrollpixels  = (int) MathUtils.clampDouble(currentscrollpixels-= dWheel * getScrollAmount(), 0, getMaxScrollPixels());
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (isVisible() && getBorder().containsPoint(x, y) && button == 0) {
            int dscroll = 0;
            if (y - this.border.top() < buttonsize && this.currentscrollpixels > 0) {
                dscroll = (int)((double)dscroll - this.getScrollAmount());
            } else if (this.border.bottom() - y < buttonsize) {
                dscroll = (int)((double)dscroll + this.getScrollAmount());
            }
            if (dscroll != 0) {
                this.currentscrollpixels = (int) MathUtils.clampDouble(this.currentscrollpixels + dscroll, 0.0D, this.getMaxScrollPixels());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double x, double y, int button) {
        return false;
    }

    @Override
    public void update(double mouseX, double mouseY) {

    }

    @Override
    public List<ITextComponent> getToolTip(int x, int y) {
        return null;
    }

    @Override
    public IRect setLeft(double value) {
        getBorder().setLeft(value);
        return this;
    }

    @Override
    public IRect setRight(double value) {
        getBorder().setRight(value);
        return this;
    }

    @Override
    public IRect setTop(double value) {
        getBorder().setTop(value);
        return this;
    }

    @Override
    public IRect setBottom(double value) {
        getBorder().setBottom(value);
        return this;
    }

    @Override
    public IRect setWidth(double value) {
        getBorder().setWidth(value);
        return this;
    }

    @Override
    public IRect setHeight(double value) {
        getBorder().setHeight(value);
        return this;
    }
}
