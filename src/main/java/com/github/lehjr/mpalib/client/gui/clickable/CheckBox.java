package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableTile;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.client.sound.Musique;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CheckBox implements IClickable{
    protected IPressable onPressed;
    protected IReleasable onReleased;

    protected boolean isVisible;
    protected boolean isEnabled;

    protected boolean isChecked;
    protected DrawableTile tile;

    String label;
    final int id;

    public CheckBox(int id, Point2D position, String displayString, boolean isChecked){
        this.id = id;
        Point2D ul = position.plus(4, 4);
        this.tile = new DrawableTile(ul, ul.plus(8, 8), Colour.BLACK, Colour.DARKGREY);
        this.label = displayString;
        this.isChecked = isChecked;
        this.enableAndShow();
        tile.setSmoothing(false);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if(this.isVisible) {
            tile.draw();
            if (isChecked) {
                Renderer.drawString("x", tile.centerx() - 2, tile.centery() - 5, Colour.WHITE);
            }
            Renderer.drawString(label, tile.centerx() + 8, tile.centery() - 4, Colour.WHITE);
        }
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (this.isVisible() && this.isEnabled()) {
            return x >= this.tile.left() && x <= this.tile.right() && y >= this.tile.top() && y <= this.tile.bottom();
        } else {
            return false;
        }
    }

    @Override
    public void move(double x, double y) {
        this.setPosition(getPosition().plus(x, y));
    }

    @Override
    public Point2D getPosition() {
        return tile.center();
    }

    @Override
    public void setPosition(Point2D position) {
        Point2D ul = position.plus(4, 4);
        tile.setLeft(ul.getX());
        tile.setTop(ul.getY());
    }

    @Override
    public List<ITextComponent> getToolTip() {
        return null;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void setOnPressed(IPressable iPressable) {
        this.onPressed = iPressable;
    }

    @Override
    public void setOnReleased(IReleasable iReleasable) {
        this.onReleased = iReleasable;
    }

    @Override
    public void onReleased() {
        if (this.isVisible && this.isEnabled && this.onReleased != null) {
            this.onReleased.onReleased(this);
        }
    }

    @Override
    public void onPressed() {
        if (this.isVisible && this.isEnabled) {
            Musique.playClientSound(SoundEvents.UI_BUTTON_CLICK, 1);
            this.isChecked = !this.isChecked;
            if (this.onPressed != null) {
                this.onPressed.onPressed(this);
            }
        }
    }
}
