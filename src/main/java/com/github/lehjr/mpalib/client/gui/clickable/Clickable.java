package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.clickable.IClickable;

import java.util.List;

/**
 * Defines a generic clickable itemStack for a MPALibGui.
 *
 * @author MachineMuse
 */
public abstract class Clickable implements IClickable {
    protected Point2D position;
    IPressable onPressed;
    IReleasable onReleased;

    public Clickable() {
        position = new Point2D(0, 0);
    }

    public Clickable(Point2D point) {
        position = point;
    }

    @Override
    public Point2D getPosition() {
        return position;
    }

    public void setPosition(Point2D position) {
        this.position = position;
    }

    @Override
    public void move(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public void move(Point2D position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    @Override
    public void setOnPressed(IPressable onPressed) {
        this.onPressed = onPressed;
    }

    @Override
    public void setOnReleased(IReleasable onReleased) {
        this.onReleased = onReleased;
    }

    @Override
    public void onPressed() {
        this.onPressed.onPressed(this);
    }

    @Override
    public void onReleased() {
        this.onReleased.onReleased(this);
    }

    @Override
    public List<String> getToolTip() {
        return null;
    }
}