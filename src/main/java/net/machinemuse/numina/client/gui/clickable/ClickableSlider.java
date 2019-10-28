package net.machinemuse.numina.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.gui.clickable.Clickable;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.math.MathUtils;
import net.minecraft.client.resources.I18n;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:08 AM, 06/05/13
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableSlider extends Clickable {
    final int cornersize = 3;
    private double valueInternal = 0;
    Point2D pos;
    double width;
    private String id;
    private String label;
    DrawableRect insideRect;
    DrawableRect outsideRect;

    public ClickableSlider(Point2D pos, double width, String id, String label) {
        this.pos = pos;
        this.width = width;
        this.id = id;
        this.position = pos;
        this.insideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + 8, 0, position.getY() + 16, Colour.LIGHTBLUE, Colour.ORANGE);
        this.outsideRect = new DrawableRect(position.getX() - width / 2.0 - cornersize, position.getY() + 8, position.getX() + width / 2.0 + cornersize, position.getY() + 16, Colour.LIGHTBLUE, Colour.DARKBLUE);
        this.label = label;
    }

    public String id() {
        return this.id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public void draw() {
        Renderer.drawCenteredString(I18n.format(label), position.getX(), position.getY());
        this.insideRect.setRight(position.getX() + width * (getValue() - 0.5) + cornersize);
        this.outsideRect.draw();
        this.insideRect.draw();
    }

    @Override
    public boolean hitBox(double x, double y) {
        return Math.abs(position.getX() - x) < width / 2 &&
                Math.abs(position.getY() + 12 - y) < 4;
    }

    public double getValue() {
        return valueInternal;
    }

    public void setValue(double v) {
        valueInternal = v;
    }

    public void setValueByX(double x) {
        valueInternal = MathUtils.clampDouble((x - pos.getX()) / width + 0.5, 0, 1);
    }
}
