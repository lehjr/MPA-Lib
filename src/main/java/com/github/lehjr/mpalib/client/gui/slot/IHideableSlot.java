package com.github.lehjr.mpalib.client.gui.slot;

import com.github.lehjr.mpalib.client.gui.geometry.Point2F;

public interface IHideableSlot {
    void enable();

    void disable();

    boolean isEnabled();

    void setPosition(Point2F position);
}
