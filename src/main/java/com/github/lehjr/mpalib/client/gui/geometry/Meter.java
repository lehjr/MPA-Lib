package com.github.lehjr.mpalib.client.gui.geometry;

import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;

import java.nio.FloatBuffer;

public class Meter extends DrawableRect {
    Colour meterColour;

    public Meter(Colour meterColourIn) {
        super(new Point2F(0, 0), new Point2F(0, 0), Colour.DARKGREY.withAlpha(0.3F), Colour.LIGHTGREY.withAlpha(0.8F));
        setWidth(32);
        setHeight(8);
        setSecondBackgroundColour(Colour.WHITE.withAlpha(0.3F));
        setShrinkBorder(true);
        this.meterColour = meterColourIn;
    }

    public Meter setMeterColour(Colour meterColourIn) {
        this.meterColour = meterColourIn;
        return this;
    }

    void setUL(float x, float y) {
        this.setTop(y);
        this.setLeft(x);
    }

    @Override
    public float getCornerradius() {
        return 0;
    }

    float value = 0;

    public void draw(MatrixStack matrixStack, float x, float y, float zLevel, float valueIn) {
        this.setUL(x, y);
        this.value = MathUtils.clampFloat(valueIn, 0, 1);
        this.draw(matrixStack, zLevel);
    }

    FloatBuffer getMeterVertices() {
        float right = finalLeft() + (finalWidth() - 1) * value;
        return preDraw(this.finalLeft() +1, this.finalTop() + 1, right, this.finalBottom() -1);
    }

    public void draw(MatrixStack matrixStack,float zLevel) {
        System.out.println("fixme!!!");

//        this.zLevel = zLevel;
//
//        // background
//        FloatBuffer backgroundVertices = this.preDraw(0.0F);
//        FloatBuffer backgroundColours = GradientAndArcCalculator.getColourGradient(this.backgroundColour, this.backgroundColour2, backgroundVertices.limit() * 4);
//        this.drawBackground(backgroundVertices, backgroundColours);
//
//        // meter
//        FloatBuffer meterVertices = this.getMeterVertices();
//        this.drawBuffer(meterVertices, meterColour, GL11.GL_TRIANGLE_FAN);
//
//        // frame
//        if (this.shrinkBorder) {
//            backgroundVertices = this.preDraw(1.0F);
//        } else {
//            backgroundVertices.rewind();
//        }
//
//        this.drawBorder(backgroundVertices);
    }
}
