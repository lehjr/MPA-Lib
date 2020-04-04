package com.github.lehjr.mpalib.client.gui.geometry;

/**
 * Base class for Points. The main reason for this is to have a
 * pass-by-reference coordinate with getter/setter functions so that points with
 * more elaborate behaviour can be implemented - such as for open/close
 * animations.
 *
 * @author MachineMuse
 */
public class Point2F {
    protected float x;
    protected float y;

    public Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Point2F(Point2F p) {
        this(p.x, p.y);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Point2F plus(Point2F b) {
        return new Point2F(getX() + b.getX(), getY() + b.getY());
    }

    public Point2F plus(float x, float y) {
        return new Point2F(getX() + x, getY() + y);
    }

    public Point2F minus(Point2F b) {
        return new Point2F(getX() - b.getX(), getY() - b.getY());
    }

    public Point2F minus(float x, float y) {
        return new Point2F(getX() - x, getY() - y);
    }

    public Point2F times(float scalefactor) {
        return new Point2F(getX() * scalefactor, getY() * scalefactor);
    }

    public boolean equals(Point2F other) {
        return this.getX() == other.getX() && this.getY() == other.getY();
    }

    public float distance() {
        return (float) Math.sqrt(getX() * getX() + getY() * getY());
    }

    public float distanceTo(Point2F position) {
        return (float) Math.sqrt(distanceSq(position));
    }

    public float distanceSq(Point2F position) {
        float xdist = position.getX() - this.getX();
        float ydist = position.getY() - this.getY();
        return xdist * xdist + ydist * ydist;
    }

    public Point2F normalize() {
        float distance = distance();
        return new Point2F(getX() / distance, getY() / distance);
    }

    public Point2F midpoint(Point2F target) {
        return new Point2F((this.getX() + target.getX()) / 2, (this.getY() + target.getY()) / 2);
    }

    public Point2F copy() {
        return new Point2F(this.getX(), this.getY());
    }

    @Override
    public String toString() {
        return "x: " + x + ", y: " + y;
    }
}