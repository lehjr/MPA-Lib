package com.github.lehjr.mpalib.client.gui.geometry;

public class FlyFromPointToPoint2F extends Point2F {
    protected final Point2F prev;
    protected final long spawnTime;
    protected final float timeTo;

    public FlyFromPointToPoint2F(float x, float y, float x2, float y2, float timeTo) {
        super(x2, y2);
        prev = new Point2F(x, y);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
    }

    public FlyFromPointToPoint2F(Point2F prev, Point2F target, float timeTo) {
        this(prev.getX(), prev.getY(), target.getX(), target.getY(), timeTo);
    }

    @Override
    public float getX() {
        return doRatio(prev.x, x);
    }

    @Override
    public float getY() {
        return doRatio(prev.y, y);
    }

    public float doRatio(float val1, float val2) {
        long elapsed = System.currentTimeMillis() - spawnTime;
        float ratio = elapsed / timeTo;
        if (ratio > 1.0F) {
            return val2;
        } else {
            return val2 * ratio + val1 * (1 - ratio);
        }
    }
}