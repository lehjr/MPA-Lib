package com.github.lehjr.mpalib.util.client.gui.slot;

import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;

public interface IHideableSlot {
    void enable();

    void disable();

    boolean isEnabled();

    void setPosition(Point2D position);
}