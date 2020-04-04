package com.github.lehjr.mpalib.client.gui.geometry;

public class SpiralPointToPoint2F extends Point2F {
    protected final Point2F center;
    protected final long spawnTime;
    protected final float timeTo;
    protected final boolean outwards;
    protected final double spiral = Math.PI;
    protected float radius;
    protected float rotation;

    public SpiralPointToPoint2F(float x, float y,
                                float x2, float y2,
                                float timeTo, boolean outwards) {
        super(x2, y2);
        center = new Point2F(x, y);
        this.radius = this.distanceTo(center);
        this.rotation = (float) Math.atan2(y2 - y, x2 - x);
        spawnTime = System.currentTimeMillis();
        this.timeTo = timeTo;
        this.outwards = outwards;
    }

    public SpiralPointToPoint2F(float x, float y,
                                float x2, float y2,
                                float timeTo) {
        this(x, y, x2, y2, timeTo, true);
    }

    public SpiralPointToPoint2F(Point2F center,
                                Point2F target,
                                float timeTo, boolean outwards) {
        this(center.getX(), center.getY(), target.getX(), target.getY(), timeTo, outwards);
    }

    public SpiralPointToPoint2F(Point2F center,
                                Point2F target,
                                float timeTo) {
        this(center, target, timeTo, true);
    }

    public SpiralPointToPoint2F(Point2F center,
                                float radius, float rotation,
                                float timeTo, boolean outward) {
        this(center.getX(), center.getY(), (float) (radius * Math.cos(rotation)), (float)(radius * Math.sin(rotation)), timeTo, outward);
        this.radius = radius;
        this.rotation = rotation;
    }

    public SpiralPointToPoint2F(Point2F center,
                                float radius, float rotation,
                                float timeTo) {
        this(center, radius, rotation, timeTo, true);
    }

    private float getRatio() {
        long elapsed = System.currentTimeMillis() - spawnTime;
        float ratio = elapsed / timeTo;
        if (ratio > 1.0F) {
            ratio = 1.0F;
        }
        if (outwards) {
            return ratio;
        } else {
            return 1.0F - ratio;
        }
    }

    private float getTheta() {
        return (float) (rotation + (spiral * (1.0F - getRatio())));
    }

    @Override
    public float getX() {
        //getX = r × cos(θ)
        return (float) (center.getX() + (radius * getRatio()) * Math.cos(getTheta()));
    }

    @Override
    public float getY() {
        //getY = r × sin(θ)
        return (float) (center.getY() + (radius * getRatio()) * Math.sin(getTheta()));
    }

    @Override
    public String toString() {
        return "\ntarget.X: " + x + ", target.Y: " + y +
                "\nactualX: " + getX() + "actualX: " + getY() +
                "\nrotation: " + rotation +
                "\nradius: " + radius;
    }
}