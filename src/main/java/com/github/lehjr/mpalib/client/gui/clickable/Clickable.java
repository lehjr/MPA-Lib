package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.Point2D;

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
    boolean enabled = true;
    boolean isVisible = true;

    public Clickable() {
        position = new Point2D(0, 0);
    }

    public Clickable(Point2D point) {
        position = point;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void enable() {
        this.enabled = true;
    }

    @Override
    public void disable() {
        this.enabled = false;
    }
    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void hide() {
        this.isVisible = false;
    }

    @Override
    public void show() {
        this.isVisible = true;
    }

    @Override
    public void enableAndShow() {
        this.enable();
        this.show();
    }

    @Override
    public void disableAndHide() {
        this.disable();
        this.hide();
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