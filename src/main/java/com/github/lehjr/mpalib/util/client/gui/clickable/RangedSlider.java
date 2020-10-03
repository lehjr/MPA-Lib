package com.github.lehjr.mpalib.util.client.gui.clickable;

import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.util.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.util.math.Colour;
import com.github.lehjr.mpalib.util.math.MathUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

public class RangedSlider extends Clickable {
    final int cornersize = 3;

    private final double height = 16;
    protected double width;
    private String label;

    DrawableRect insideRect;
    DrawableRect outsideRect;

    /**
     * Is this slider control being dragged.
     */
    public boolean dragging = false;

    /**
     * The value of this slider control. Based on a value representing 0 - 100%
     */
    public double sliderValue = 1.0F;
    public double minValue = 0.0F;
    public double maxValue = 5.0F;

    @Nullable
    public ISlider parent = null;

    public RangedSlider(Point2D position, double width, String label, double minVal, double maxVal, double currentVal) {
        this(position, width, label, minVal, maxVal, currentVal, null);
    }

    public RangedSlider(Point2D position, String label, double minVal, double maxVal, double currentVal, ISlider par) {
        this(position, 150, label, minVal, maxVal, currentVal, par);
    }

    public RangedSlider(Point2D position, double width, String label, double minVal, double maxVal, double currentVal, @Nullable ISlider iSlider) {
        this.width = width;
        this.position = position;
        this.label = label;
        createNewRects();
        minValue = minVal;
        maxValue = maxVal;
        sliderValue = (currentVal - minValue) / (maxValue - minValue);
        parent = iSlider;
    }

    void createNewRects() {
        this.insideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + height * 0.5F, 0, position.getY() + height, Colour.ORANGE, Colour.LIGHTBLUE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + height * 0.5F, position.getX() + width / 2.0F + cornersize, position.getY() + height, Colour.DARKBLUE, Colour.LIGHTBLUE);
        this.insideRect.setWidth(6);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float zLevel) {
        if (this.isVisible) {
            if (label != null) {
                Renderer.drawCenteredString(matrixStack, I18n.format(label), position.getX(), position.getY());
            }

            this.outsideRect.draw(matrixStack, zLevel);
            this.insideRect.setPosition(new Point2D(this.position.getX() + this.width * (this.sliderValue - 0.5F), this.outsideRect.centery()));
            this.insideRect.draw(matrixStack, zLevel);
        }
    }

    public void update(double mouseX, double mouseY) {
        double siderStart = this.sliderValue;
        if (dragging && this.isEnabled() && this.isVisible() && this.hitBox(mouseX, mouseY)) {
            this.sliderValue = MathUtils.clampDouble((mouseX - this.position.getX()) / (this.width -3) + 0.5, 0.0, 1.0);
        } else {
            this.sliderValue = MathUtils.clampDouble(sliderValue, 0.0, 1.0);
        }

        if (siderStart != sliderValue && parent != null) {
            parent.onChangeSliderValue(this);
        }
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


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        update((double)mouseX, (double) mouseY);
        this.dragging = false;
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEnabled() && this.isVisible() && this.hitBox((double)mouseX, (double)mouseY)) {
            update((double)mouseX, (double)mouseY);
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