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

package com.github.lehjr.mpalib.client.gui.geometry;

import com.github.lehjr.mpalib.math.Colour;
import com.mojang.blaze3d.platform.GlStateManager;
import org.lwjgl.opengl.GL11;

public class DrawableRectangularGrid extends DrawableRelativeRect {
    Colour gridColour;
    int gridHeight;
    int gridWidth;
    Float horizontalSegmentSize;
    Float verticleSegmentSize;
    final RelativeRect[] boxes;

    public DrawableRectangularGrid(double left, double top, double right, double bottom, boolean growFromMiddle,
                                   Colour insideColour,
                                   Colour outsideColour,
                                   Colour gridColour,
                                   int gridHeight,
                                   int gridWidth) {
        super(left, top, right, bottom, growFromMiddle, insideColour, outsideColour);
        this.gridColour = gridColour;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.boxes = new RelativeRect[gridHeight*gridWidth];
        setBoxes();
    }

    public DrawableRectangularGrid(double left, double top, double right, double bottom,
                                   Colour insideColour,
                                   Colour outsideColour,
                                   Colour gridColour,
                                   int gridHeight,
                                   int gridWidth) {
        super(left, top, right, bottom, false, insideColour, outsideColour);
        this.gridColour = gridColour;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.boxes = new RelativeRect[gridHeight*gridWidth];
        setBoxes();
    }

    public DrawableRectangularGrid(Point2D ul, Point2D br,
                                   Colour insideColour,
                                   Colour outsideColour,
                                   Colour gridColour,
                                   int gridHeight,
                                   int gridWidth) {
        super(ul, br, insideColour, outsideColour);
        this.gridColour = gridColour;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.boxes = new RelativeRect[gridHeight*gridWidth];
        setBoxes();
    }

    public DrawableRectangularGrid(RelativeRect ref,
                                   Colour insideColour,
                                   Colour outsideColour,
                                   Colour gridColour,
                                   int gridHeight,
                                   int gridWidth) {
        super(ref.left(), ref.top(), ref.right(), ref.bottom(), ref.growFromMiddle(), insideColour, outsideColour);
        this.gridColour = gridColour;
        this.gridHeight = gridHeight;
        this.gridWidth = gridWidth;
        this.boxes = new RelativeRect[gridHeight*gridWidth];
        setBoxes();
    }

    void setBoxes() {
        for (int i = 0; i < boxes.length; i++) {
            boxes[i] = new RelativeRect(0, 0, 0, 0);
        }
    }


    public RelativeRect[] getBoxes() {
        return boxes;
    }

    void setupGrid() {
        horizontalSegmentSize = (float) (width() / gridWidth);
        verticleSegmentSize = (float) (height() / gridHeight);

        // uper left coner
        Point2D box_ul;
        // bottom right
        Point2D box_br;
        // width and height of each box

        Point2D box_offset = new Point2D(horizontalSegmentSize, verticleSegmentSize);
        int i = 0;

        // These boxes provide centers for the slots
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                box_ul = new Point2D(horizontalSegmentSize * x, verticleSegmentSize * y);
                boxes[i].setTargetDimensions(box_ul, box_offset);

                if (i >0) {
                    if (x > 0)
                        boxes[i].setMeRightOf(boxes[i-1]);
                    if (y > 0){
                        boxes[i].setMeBelow(boxes[i-gridWidth]);
                    }
                }
                i++;
            }
        }
    }

    void drawGrid() {

        // reinitialize values on "growFromCenter" or resize
        boolean needInt = false;
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] == null) {
                needInt = true;
                break;
            }
        }

        if (needInt)
            setBoxes();

        if (horizontalSegmentSize == null || verticleSegmentSize == null || (this.ul != this.ulFinal || this.wh != this.whFinal)) {
            setupGrid();
        }

        GlStateManager.lineWidth(1f);
        gridColour.doGL();
        GlStateManager.begin(GL11.GL_LINES);

        // Horizontal lines
        if (gridHeight >1)
            for (float y = (float) (verticleSegmentSize + top()); y < bottom(); y+= verticleSegmentSize) {
                GlStateManager.vertex3f((float)left(), y, 1);
                GlStateManager.vertex3f((float)right(), y, 1);
            }

        // Vertical lines
        if(gridWidth > 1)
            for (float x = (float) (horizontalSegmentSize + left()); x < right(); x += horizontalSegmentSize ) {
                GlStateManager.vertex3f(x, (float)top(), 1);
                GlStateManager.vertex3f(x, (float)bottom(), 1);
            }
        Colour.WHITE.doGL();
        GlStateManager.end();
    }

    @Override
    public DrawableRelativeRect setLeft(double value) {
        double diff = value - left();
        super.setLeft(value);
        for (RelativeRect box : boxes) {
            if (box != null)
                box.setLeft(box.left() + diff);
        }
        return this;
    }

    @Override
    public void preDraw() {
        GlStateManager.lineWidth(1f);
        super.preDraw();
    }

    @Override
    public void draw() {
        float lineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        boolean smooth  = GL11.glIsEnabled(GL11.GL_LINE_SMOOTH);
        preDraw();
        drawBackground();
        drawGrid();
        drawBorder();
        postDraw();
        if (!smooth)
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glLineWidth(lineWidth);
    }
}