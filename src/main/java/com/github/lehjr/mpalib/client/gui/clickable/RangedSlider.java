package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2F;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nullable;

public class RangedSlider extends Clickable {
    final int cornersize = 3;

    private final float height = 16;
    protected float width;
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
    public float sliderValue = 1.0F;
    public float minValue = 0.0F;
    public float maxValue = 5.0F;

    @Nullable
    public ISlider parent = null;

    public RangedSlider(int id, Point2F position, float width, String label, float minVal, float maxVal, float currentVal) {
        this(id, position, width, label, minVal, maxVal, currentVal, null);
    }

    public RangedSlider(int id, Point2F position, String label, float minVal, float maxVal, float currentVal, ISlider par) {
        this(id, position, 150, label, minVal, maxVal, currentVal, par);
    }

    public RangedSlider(int id, Point2F position, float width, String label, float minVal, float maxVal, float currentVal, @Nullable ISlider iSlider) {
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
        this.insideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + height * 0.5F, 0, position.getY() + height, Colour.ORANGE, Colour.LIGHTBLUE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0F - cornersize, position.getY() + height * 0.5F, position.getX() + width / 2.0F + cornersize, position.getY() + height, Colour.DARKBLUE, Colour.LIGHTBLUE);
        this.insideRect.setWidth(6);
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks, float zLevel) {
        if (this.isVisible) {
            if (label != null) {
                Renderer.drawCenteredString(I18n.format(label), position.getX(), position.getY());
            }

            this.outsideRect.draw(zLevel);
            this.insideRect.setPosition(new Point2F(this.position.getX() + this.width * (this.sliderValue - 0.5F), this.outsideRect.centery()));
            this.insideRect.draw(zLevel);
        }
    }

    public void update(float mouseX, float mouseY) {
        float siderStart = this.sliderValue;
        if (dragging && this.isEnabled() && this.isVisible() && this.hitBox(mouseX, mouseY)) {
            this.sliderValue = MathUtils.clampFloat((mouseX - this.position.getX()) / (this.width -3) + 0.5F, 0.0F, 1.0F);
        } else {
            this.sliderValue = MathUtils.clampFloat(sliderValue, 0.0F, 1.0F);
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
    public void setPosition(Point2F position) {
        super.setPosition(position);
        createNewRects();
    }

    public void setWidth(float widthIn) {
        this.width = widthIn;
        createNewRects();
    }

    public int getValueInt() {
        return (int) Math.round(sliderValue * (maxValue - minValue) + minValue);
    }

    public float getValue() {
        return sliderValue * (maxValue - minValue) + minValue;
    }

    public void setValue(float d) {
        this.sliderValue = (d - minValue) / (maxValue - minValue);
    }

    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */


    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        update((float)mouseX, (float) mouseY);
        this.dragging = false;
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isEnabled() && this.isVisible() && this.hitBox((float)mouseX, (float)mouseY)) {
            update((float)mouseX, (float)mouseY);
            this.dragging = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean hitBox(float x, float y) {
        return Math.abs(position.getX() - x) < width / 2 &&
                Math.abs(position.getY() + 12 - y) < 4;
    }

    public interface ISlider {
        void onChangeSliderValue(RangedSlider slider);
    }
}