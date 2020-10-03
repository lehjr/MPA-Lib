package com.github.lehjr.mpalib.util.client.gui.geometry;

public class FlyFromPointToPoint2D extends Point2D {
    protected final Point2D prev;
    protected final long spawnTime;
    protected final double timeTo;

    public FlyFromPointToPoint2D(double x, double y, double x2, double y2, double timeTo) {
        super(x2, y2);
        this.prev = new Point2D(x, y);
        this.spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
    }

    public FlyFromPointToPoint2D(Point2D prev, Point2D target, double timeTo) {
        this(prev.getX(), prev.getY(), target.getX(), target.getY(), timeTo);
    }

    public double getX() {
        return this.doRatio(this.prev.x, this.x);
    }

    public double getY() {
        return this.doRatio(this.prev.y, this.y);
    }

    public double doRatio(double val1, double val2) {
        long elapsed = System.currentTimeMillis() - this.spawnTime;
        double ratio = (double)elapsed / this.timeTo;
        return ratio > 1.0D ? val2 : val2 * ratio + val1 * (1.0D - ratio);
    }
}