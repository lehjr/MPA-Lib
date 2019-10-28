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

import com.github.lehjr.mpalib.client.gui.clickable.IClickable;
import com.github.lehjr.mpalib.client.gui.frame.IGuiFrame;
import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class ContainerlessGui extends GuiScreen {
    protected long creationTime;
    /** The X size of the inventory window in pixels. */
    public int xSize = 176;
    /** The Y size of the inventory window in pixels. */
    public int ySize = 166;
    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    public int guiLeft;
    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    public int guiTop;

    protected List<IGuiFrame> frames;

    protected DrawableRect backgroundRect;

    public ContainerlessGui() {
        super();
        frames = new ArrayList();
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui() {
        super.initGui();
        this.frames.clear();
        // this.controlList.clear();
        Keyboard.enableRepeatEvents(true);
        creationTime = System.currentTimeMillis();

        int xpadding = (width - getxSize()) / 2;
        int ypadding = (height - ySize) / 2;
        backgroundRect = new DrawableRect(absX(-1), absY(-1), absX(1), absY(1), true, new Colour(0.1F, 0.9F, 0.1F, 0.8F), new Colour(0.0F, 0.2F,
                0.0F, 0.8F));
    }









    /**
     * Draws the gradient-rectangle background you see in the TinkerTable gui.
     */
    public void drawRectangularBackground() {
        backgroundRect.draw();
    }

    /**
     * Adds a frame to this gui's draw list.
     *
     * @param frame
     */
    public void addFrame(IGuiFrame frame) {
        frames.add(frame);
    }


//    /**
//     * Draws all clickables in a list
//     */
//    public void drawClickables(List<? extends IClickable> list, int mouseX, int mouseY, float partialTicks) {
//        if (list == null) {
//            return;
//        }
//        for (IClickable clickie : list) {
//            clickie.render(mouseX, mouseY, partialTicks);
//        }
//    }


    /**
     * Draws the background layer for the GUI.
     */
    public void drawBackground() {
        this.drawDefaultBackground(); // Shading on the world view
        this.drawRectangularBackground(); // The window rectangle
    }

    /**
     * Called every frame, draws the screen!
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        update(mouseX, mouseY);
        drawBackground();
        renderFrames(mouseX, mouseY, partialTicks);
        drawToolTip(mouseX, mouseY);
    }


    // new
    public void update(double x, double y) {
        for (IGuiFrame frame : frames) {
            frame.update(x, y);
        }
    }

    public void renderFrames(int mouseX, int mouseY, float partialTicks) {
        for (IGuiFrame frame : frames) {
            frame.render(mouseX, mouseY, partialTicks);
        }
    }

//    /**
//     * Draws all clickables in a list!
//     */
//    public void drawClickables(List<? extends IClickable> list) {
//        if (list == null) {
//            return;
//        }
//        Iterator<? extends IClickable> iter = list.iterator();
//        IClickable clickie;
//        while (iter.hasNext()) {
//            clickie = iter.next();
//            clickie.render();
//        }
//    }


    /**
     * Returns the first ID in the list that is hit by a click
     *
     * @return
     */
    public int hitboxClickables(int x, int y, List<? extends IClickable> list) {
        if (list == null) {
            return -1;
        }
        IClickable clickie;
        for (int i = 0; i < list.size(); i++) {
            clickie = list.get(i);
            if (clickie.hitBox(x, y)) {
                // MuseLogger.logDebug("Hit!");
                return i;
            }
        }
        return -1;
    }

    /**
     * Called when the mouse is clicked.
     */
    /**
     * Called when the mouse is clicked.
     */
    @Override
    public void mouseClicked(int x, int y, int button) {
        for (IGuiFrame frame : frames) {
            frame.onMouseDown(x, y, button);
        }
    }

    /**
     * Called when the mouse is moved or a mouse button is released. Signature:
     * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
     * mouseUp
     */
    @Override
    public void mouseReleased(int x, int y, int which) {
        for (IGuiFrame frame : frames) {
            frame.onMouseUp(x, y, which);
        }
    }

//    @Deprecated
//    public void drawToolTip() {
//        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
//        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
//
//        drawToolTip(mouseX, mouseY);
//    }

    public void drawToolTip(int mouseX, int mouseY) {
        List<String> tooltip = getToolTip(mouseX, mouseY);
        if (tooltip != null) {
            double strwidth = 0;
            for (String s : tooltip) {
                double currstrwidth = Renderer.getStringWidth(s);
                if (currstrwidth > strwidth) {
                    strwidth = currstrwidth;
                }
            }

            FontRenderer font = Minecraft.getMinecraft().fontRenderer;
            this.drawHoveringText(tooltip, mouseX, mouseY, (font == null ? fontRenderer : font));
        }
    }

    public List<String> getToolTip(int x, int y) {
        List<String> hitTip;
        for (IGuiFrame frame : frames) {
            hitTip = frame.getToolTip(x, y);
            if (hitTip != null) {
                return hitTip;
            }
        }
        return null;
    }

    public void refresh() {
    }

    /**
     * Whether or not this gui pauses the game in single player.
     */
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    /**
     * Returns absolute screen coordinates (int 0 to width) from a relative
     * coordinate (float -1.0F to +1.0F)
     *
     * @param relx Relative X coordinate
     * @return Absolute X coordinate
     */
    public int absX(double relx) {
        int absx = (int) ((relx + 1) * getxSize() / 2);
        int xpadding = (width - getxSize()) / 2;
        return absx + xpadding;
    }

    /**
     * Returns relative coordinate (float -1.0F to +1.0F) from absolute
     * coordinates (int 0 to width)
     */
    public int relX(double absx) {
        int padding = (width - getxSize()) / 2;
        return (int) ((absx - padding) * 2 / getxSize() - 1);
    }

    /**
     * Returns absolute screen coordinates (int 0 to width) from a relative
     * coordinate (float -1.0F to +1.0F)
     *
     * @param rely Relative Y coordinate
     * @return Absolute Y coordinate
     */
    public int absY(double rely) {
        int absy = (int) ((rely + 1) * ySize / 2);
        int ypadding = (height - ySize) / 2;
        return absy + ypadding;
    }

    /**
     * Returns relative coordinate (float -1.0F to +1.0F) from absolute
     * coordinates (int 0 to width)
     */
    public int relY(float absy) {
        int padding = (height - getYSize()) / 2;
        return (int) ((absy - padding) * 2 / getYSize() - 1);
    }

    /**
     * @return the xSize
     */
    public int getxSize() {
        return xSize;
    }

    /**
     * @param xSize the xSize to set
     */
    public void setxSize(int xSize) {
        this.xSize = xSize;
    }

    /**
     * @return the ySize
     */
    public int getySize() {
        return ySize;
    }

    /**
     * @param ySize the ySize to set
     */
    public void setySize(int ySize) {
        this.ySize = ySize;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public int getXSize() {
        return xSize;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
        this.guiLeft = (this.width - getXSize()) / 2;
    }

    public int getYSize() {
        return ySize;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
        this.guiTop = (this.height - getYSize()) / 2;
    }
}