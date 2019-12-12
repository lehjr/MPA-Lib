/*
 * MPA-Lib (Formerly known as Numina)
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
import com.github.lehjr.mpalib.client.gui.geometry.DrawableTile;
import com.github.lehjr.mpalib.client.gui.geometry.IRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.slot.UniversalSlot;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

import java.util.ArrayList;
import java.util.List;

public class InventoryFrame implements IGuiFrame {
    Container container;
    protected DrawableRect border;
    Colour backgroundColour;
    Colour gridColour;
    public final int gridWidth;
    public final int gridHeight;
    List<Integer> slotIndexes;
    List<DrawableTile> tiles;
    Point2D slot_ulShift = new Point2D(0, 0);

    public InventoryFrame(Container containerIn,
                          Point2D topleft,
                          Point2D bottomright,
                          Colour backgroundColour,
                          Colour borderColour,
                          Colour gridColourIn,
                          int gridWidth,
                          int gridHeight,
                          List<Integer> slotIndexesIn) {
        this.container = containerIn;
        this.border = new DrawableRect(topleft, bottomright, backgroundColour, borderColour);
        this.backgroundColour = backgroundColour;
        this.gridColour = gridColourIn;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.slotIndexes = slotIndexesIn;
        this.tiles = new ArrayList<>();
    }

    public void loadSlots() {
        Point2D wh = new Point2D(18, 18);
        Point2D ul = new Point2D(border.left(), border.top());
        tiles = new ArrayList<>();
        int i = 0;
        outerLoop:
        for(int row = 0; row < gridHeight; row ++) {
            for (int col = 0; col < gridWidth; col ++) {
                if (i == slotIndexes.size()){
                    break outerLoop;
                }
                DrawableTile tile = new DrawableTile(ul, ul.plus(wh), backgroundColour, gridColour);
                tiles.add(tile);

                if (i > 0) {
                    if (col > 0) {
                        this.tiles.get(i).setMeRightOf(this.tiles.get(i - 1));
                    }

                    if (row > 0) {
                        this.tiles.get(i).setMeBelow(this.tiles.get(i - this.gridWidth));
                    }
                }
                Point2D position = tile.center().copy().minus(slot_ulShift);

                Slot slot = container.getSlot(slotIndexes.get(i));
                if (slot instanceof UniversalSlot) {
                    ((UniversalSlot) slot).setPosition(position);
                } else {
                    slot.xPos = (int)position.getX();
                    slot.yPos = (int)position.getY();
                }
                i++;
            }
        }
    }

    @Override
    public boolean onMouseDown(double mouseX, double mouseY, int button) {
        return this.border.containsPoint(mouseX, mouseY);
    }

    @Override
    public boolean onMouseUp(double mouseX, double mouseY, int button) {
        return this.border.containsPoint(mouseX, mouseY);
    }

    public Point2D getUlShift() {
        return slot_ulShift;
    }

    public void setUlShift(Point2D ulShift) {
        this.slot_ulShift = ulShift;
    }

    @Override
    public void init(double left, double top, double right, double bottom) {
        this.border.setTargetDimensions(left, top, right, bottom);
        loadSlots();
    }

    @Override
    public void update(double v, double v1) {
        loadSlots();
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        border.preDraw();
        border.drawBackground();

        if (this.tiles != null && !this.tiles.isEmpty()) {
            for (DrawableTile tile : tiles) {
                tile.draw();
            }
        }

        border.drawBorder();
        this.border.postDraw();
    }

    @Override
    public List<String> getToolTip(int i, int i1) {
        return null;
    }

    @Override
    public IRect getBorder() {
        return this.border;
    }

    @Override
    public void setTargetDimensions(double left, double top, double right, double bottom) {
        border.setTargetDimensions(left, top, right, bottom);
        loadSlots();
    }

    @Override
    public void setTargetDimensions(Point2D ul, Point2D wh) {
        border.setTargetDimensions(ul, wh);
        loadSlots();
    }

    @Override
    public IRect setLeft(double value) {
        setLeft(value);
        loadSlots();
        return this;
    }

    @Override
    public IRect setRight(double value) {
        setRight(value);
        loadSlots();
        return this;
    }

    @Override
    public IRect setTop(double value) {
        setTop(value);
        loadSlots();
        return this;
    }

    @Override
    public IRect setBottom(double value) {
        setBottom(value);
        loadSlots();
        return this;
    }

    @Override
    public IRect setWidth(double value) {
        border.setWidth(value);
        loadSlots();
        return this;
    }

    @Override
    public IRect setHeight(double value) {
        border.setHeight(value);
        loadSlots();
        return this;
    }

    @Override
    public void move(Point2D moveAmount) {
        border.move(moveAmount);
        loadSlots();
    }

    @Override
    public void move(double x, double y) {
        border.move(x, y);
        loadSlots();
    }

    @Override
    public void setPosition(Point2D position) {
        border.setPosition(position);
        loadSlots();
    }
}