package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

public class RangedSlider extends Clickable {
    final int cornersize = 3;

    private final double height = 16;
    protected double width;
    private String label;

    DrawableRect insideRect;
    DrawableRect outsideRect;

    public int id;

    /**
     * Is this slider control being dragged.
     */
    public boolean dragging = false;

    /**
     * The value of this slider control. Based on a value representing 0 - 100%
     */
    public double sliderValue = 1.0F;
    public double minValue = 0.0D;
    public double maxValue = 5.0D;

    @Nullable
    public ISlider parent = null;

    public RangedSlider(int id, Point2D position, double width, String label, double minVal, double maxVal, double currentVal) {
        this(id, position, width, label, minVal, maxVal, currentVal, null);
    }

    public RangedSlider(int id, Point2D position, String label, double minVal, double maxVal, double currentVal, ISlider par) {
        this(id, position, 150, label, minVal, maxVal, currentVal, par);
    }

    public RangedSlider(int id, Point2D position, double width, String label, double minVal, double maxVal, double currentVal, @Nullable ISlider iSlider) {
        this.width = width;
        this.id = id;
        this.position = position;
        this.label = label;
        createNewRects();
        minValue = minVal;
        maxValue = maxVal;
        sliderValue = (currentVal - minValue) / (maxValue - minValue);
        parent = iSlider;
    }

    void createNewRects() {
        this.insideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + height * 0.5, 0, position.getY() + height, Colour.ORANGE, Colour.LIGHTBLUE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + height * 0.5, position.getX() + width / 2.0 + cornersize, position.getY() + height, Colour.DARKBLUE, Colour.LIGHTBLUE);
        this.insideRect.setWidth(6);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (this.isVisible) {
            if (label != null) {
                Renderer.drawCenteredString(I18n.format(label), position.getX(), position.getY());
            }

            this.outsideRect.draw();
            this.insideRect.setPosition(new Point2D(this.position.getX() + this.width * (this.sliderValue - 0.5D), this.outsideRect.centery()));
            this.insideRect.draw();
        }
    }

    public void update(double mouseX, double mouseY) {
        double siderStart = this.sliderValue;
        if (dragging && this.isEnabled() && this.isVisible() && this.hitBox(mouseX, mouseY)) {
            this.sliderValue = MathUtils.clampDouble((mouseX - this.position.getX()) / (this.width -3) + 0.5, 0.0D, 1.0D);
        } else {
            this.sliderValue = MathUtils.clampDouble(sliderValue, 0.0D, 1.0D);
        }

        if (siderStart != sliderValue && parent != null) {
            parent.onChangeSliderValue(this);
        }
    }

    public int id() {
        return this.id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(position);
        createNewRects();
    }

    public void setWidth(double widthIn) {
        this.width = widthIn;
        createNewRects();
    }

    public int getValueInt() {
        return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
    }

    public double getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }

    public void setValue(double d) {
        this.sliderValue = (d - minValue) / (maxValue - minValue);
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(double mouseX, double mouseY, int button) {
        update(mouseX, mouseY);
        this.dragging = false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEnabled() && this.isVisible() && this.hitBox(mouseX, mouseY)) {
            update(mouseX, mouseY);
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean hitBox(double x, double y) {
        return Math.abs(position.getX() - x) < width / 2 &&
                Math.abs(position.getY() + 12 - y) < 4;
    }

    public interface ISlider {
        void onChangeSliderValue(RangedSlider slider);
    }
}