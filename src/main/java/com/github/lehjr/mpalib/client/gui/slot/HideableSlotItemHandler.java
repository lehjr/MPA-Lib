package com.github.lehjr.mpalib.client.gui.slot;

import com.github.lehjr.mpalib.client.gui.geometry.Point2F;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class HideableSlotItemHandler extends SlotItemHandler implements IHideableSlot {
    boolean isEnabled = false;
    protected int parentSlot = -1;
    public int xPos;
    public int yPos;

    public HideableSlotItemHandler(IItemHandler itemHandler, int parent, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.parentSlot = parent;
        this.xPos = xPosition;
        this.yPos = yPosition;
    }

    public int getParentSlot(){
        return parentSlot;
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