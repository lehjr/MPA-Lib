package com.github.lehjr.mpalib.client.gui.geometry;

/**
 * @author lehjr
 */
public interface IRect {
    /**
     *  Alternative to spawning a completely new object. Especially handy for GUI's with large constructors
     */
    void setTargetDimensions(float left, float top, float right, float bottom);

    void setTargetDimensions(Point2F ul, Point2F wh);

    default Point2F center() {
        return new Point2F(centerx(), centery());
    }

    Point2F getUL();

    Point2F getULFinal();

    Point2F getWH();

    Point2F getWHFinal();

    float left();

    float finalLeft();

    float top();

    float finalTop();

    float right();

    float finalRight();

    float bottom();

    float finalBottom();

    float width();

    float finalWidth();

    float height();

    float finalHeight();

    IRect setLeft(float value);

    IRect setRight(float value);

    IRect setTop(float value);

    IRect setBottom(float value);

    IRect setWidth(float value);

    IRect setHeight(float value);

    void move(Point2F moveAmount);

    void move(float x, float y);

    void setPosition(Point2F position);

    boolean growFromMiddle();

    default boolean containsPoint(float x, float y) {
        return x > left() && x < right() && y > top() && y < bottom();
    }

    default boolean containsPoint(double x, double y) {
        return x > left() && x < right() && y > top() && y < bottom();
    }

    default float centerx() {
        return (left() + right()) / 2.0F;
    }

    default float centery() {
        return (top() + bottom()) / 2.0F;
    }
}