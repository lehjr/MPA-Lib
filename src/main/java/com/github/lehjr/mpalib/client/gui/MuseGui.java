///*
// * Copyright (c) 2019 MachineMuse, Lehjr
// * All rights reserved.
// *
// * Redistribution and use in source and binary forms, with or without
// * modification, are permitted provided that the following conditions are met:
// *
// *  * Redistributions of source code must retain the above copyright notice, this
// *    list of conditions and the following disclaimer.
// *
// *  * Redistributions in binary form must reproduce the above copyright notice,
// *    this list of conditions and the following disclaimer in the documentation
// *    and/or other materials provided with the distribution.
// *
// * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
// * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
// */
//
//package com.github.lehjr.mpalib.client.gui;
//
//import com.github.lehjr.mpalib.client.gui.clickable.IClickable;
//import com.github.lehjr.mpalib.client.gui.frame.IGuiFrame;
//import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
//import com.github.lehjr.mpalib.client.render.MPALibRenderer;
//import com.github.lehjr.mpalib.math.Colour;
//import net.minecraft.entity.player.PlayerInventory;
//import net.minecraft.inventory.container.Container;
//import net.minecraft.util.text.ITextComponent;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Used by most MPS Gui's
// */
//public class MuseGui <T extends Container> extends ContainerScreen<T> {
//    protected long creationTime;
//    protected DrawableRect tooltipRect;
//    protected List<IGuiFrame> frames;
//
//    public MuseGui(T container, PlayerInventory playerInventory, ITextComponent title) {
//        super(container, playerInventory, title);
//        frames = new ArrayList();
//        tooltipRect = new DrawableRect(0, 0, 0, 0, false,
//                new Colour(0.1F, 0.3F, 0.4F, 0.7F),
//                new Colour(0.2F, 0.6F, 0.9F, 0.7F));
//    }
//
//    @Override
//    public void init() {
//        super.init();
//        minecraft.keyboardListener.enableRepeatEvents(true);
//        creationTime = System.currentTimeMillis();
//    }
//
//    /**
//     * Draws the gradient-rectangle background you see in the TinkerTable gui.
//     */
//    public void drawRectangularBackground() {
//
//    }
//
//    /**
//     * Adds a frame to this gui's draw list.
//     *
//     * @param frame
//     */
//    public void addFrame(IGuiFrame frame) {
//        frames.add(frame);
//    }
//
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
//
//    @Override
//    public void renderBackground() {
//        super.renderBackground();
//        this.drawRectangularBackground(); // The window rectangle
//    }
//
//    /**
//     * Called every frame, draws the screen!
//     */
//    @Override
//    public void render(int mouseX, int mouseY, float partialTicks) {
//        // item lighting code in super.render method screws up this lighting
////        super.render(mouseX, mouseY, partialTicks);
//        update(mouseX, mouseY);
//
//        renderBackground();
//        for (IGuiFrame frame : frames) {
//            frame.render(mouseX, mouseY, partialTicks);
//        }
//        drawToolTip();
//    }
//
//    public void update(double x, double y) {
//        //        double x = minecraft.mouseHelper.getMouseX() * this.width / (double) this.minecraft.mainWindow.getWidth();
////        double y = minecraft.mouseHelper.getMouseY() * this.height / (double) this.minecraft.mainWindow.getHeight();
//        for (IGuiFrame frame : frames) {
//            frame.update(x, y);
//        }
//    }
//
//    @Override
//    public void tick() {
//        super.tick();
//    }
//
//    @Override
//    public boolean mouseScrolled(double mouseX, double mouseY, double dWheel) {
//        for (IGuiFrame frame : frames) {
//            if (frame.mouseScrolled(mouseX, mouseY, dWheel))
//                return true;
//        }
//        return false;
//    }
//
//    /**
//     * Called when the mouse is clicked.
//     */
//    @Override
//    public boolean mouseClicked(double x, double y, int button) {
//        for (IGuiFrame frame : frames) {
//            frame.mouseClicked(x, y, button);
//        }
//        return true;
//    }
//
//    /**
//     * Called when the mouse is moved or a mouse button is released. Signature:
//     * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
//     * mouseUp
//     */
//    @Override
//    public boolean mouseReleased(double x, double y, int which) {
//        for (IGuiFrame frame : frames) {
//            frame.mouseReleased(x, y, which);
//        }
//        return true;
//    }
//
//    protected void drawToolTip() {
//        int x = (int) (minecraft.mouseHelper.getMouseX() * this.width / this.minecraft.mainWindow.getWidth());
//        // FIXME: this is backwards
////        int y = (int) (this.height - minecraft.mouseHelper.getMouseY() * this.height / (double) this.minecraft.mainWindow.getHeight() - 1);
//        // this is probably what that should look like now
//        int y = (int) (minecraft.mouseHelper.getMouseY() * this.height / (double) this.minecraft.mainWindow.getHeight());
//        List<ITextComponent> tooltip = getToolTip(x, y);
//        if (tooltip != null) {
//            double strwidth = 0;
//            for (ITextComponent s : tooltip) {
//                double currstrwidth = MPALibRenderer.getStringWidth(s.getString());
//                if (currstrwidth > strwidth) {
//                    strwidth = currstrwidth;
//                }
//            }
//            double top, bottom, left, right;
//            if (y > this.height / 2) {
//                top = y - 10 * tooltip.size() - 8;
//                bottom = y;
//                left = x;
//                right = x + 8 + strwidth;
//            } else {
//                top = y;
//                bottom = y + 10 * tooltip.size() + 8;
//
//                left = x + 4;
//                right = x + 12 + strwidth;
//            }
//
//            tooltipRect.setTop(top);
//            tooltipRect.setLeft(left);
//            tooltipRect.setRight(right);
//            tooltipRect.setBottom(bottom);
//            tooltipRect.draw();
//            for (int i = 0; i < tooltip.size(); i++) {
//                MPALibRenderer.drawString(tooltip.get(i).getString(), left + 4, bottom - 10 * (tooltip.size() - i) - 4);
//            }
//        }
//    }
//
//    /**
//     * @return
//     */
//    public List<ITextComponent> getToolTip(int x, int y) {
//        List<ITextComponent> hitTip;
//        for (IGuiFrame frame : frames) {
//            hitTip = frame.getToolTip(x, y);
//            if (hitTip != null) {
//                return hitTip;
//            }
//        }
//        return null;
//    }
//}