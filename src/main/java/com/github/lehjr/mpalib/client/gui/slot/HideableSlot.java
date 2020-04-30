package com.github.lehjr.mpalib.client.gui.slot;

import com.github.lehjr.mpalib.client.gui.geometry.Point2F;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

public class HideableSlot extends Slot implements IHideableSlot {
    boolean isEnabled = false;
    public int xPos;
    public int yPos;

    public HideableSlot(IInventory iInventory, int slotIndex, int xPosition, int yPosition) {
        super(iInventory, slotIndex, xPosition, yPosition);
        this.xPos = xPosition;
        this.yPos = yPosition;
    }

    @Override
    public void enable() {
        this.isEnabled = true;
    }

    @Override
    public void disable() {
        this.isEnabled = false;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setPosition(Point2F position) {
        this.xPos = (int) position.getX();
        this.yPos = (int) position.getY();
    }
}