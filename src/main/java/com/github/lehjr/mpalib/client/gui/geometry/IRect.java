package com.github.lehjr.mpalib.client.gui.geometry;

/**
 * @author lehjr
 */
public interface IRect {
    /**
     *  Alternative to spawning a completely new object. Especially handy for GUI's with large constructors
     */
    void setTargetDimensions(double left, double top, double right, double bottom);

    void setTargetDimensions(Point2D ul, Point2D wh);

    default Point2D center() {
        return new Point2D(centerx(), centery());
    }

    Point2D getUL();

    Point2D getULFinal();

    Point2D getWH();

    Point2D getWHFinal();

    double left();

    double finalLeft();

    double top();

    double finalTop();

    double right();

    double finalRight();

    double bottom();

    double finalBottom();

    double width();

    double finalWidth();

    double height();

    double finalHeight();

    IRect setLeft(double value);

    IRect setRight(double value);

    IRect setTop(double value);

    IRect setBottom(double value);

    IRect setWidth(double value);

    IRect setHeight(double value);

    void move(Point2D moveAmount);

    void move(double x, double y);

    void setPosition(Point2D position);

    boolean growFromMiddle();

    default boolean containsPoint(double x, double y) {
        return x > left() && x < right() && y > top() && y < bottom();
    }

    default double centerx() {
        return (left() + right()) / 2.0;
    }

    default double centery() {
        return (top() + bottom()) / 2.0;
    }
}