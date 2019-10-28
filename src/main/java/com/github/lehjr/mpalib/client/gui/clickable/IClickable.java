package com.github.lehjr.mpalib.client.gui.clickable;

import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public interface IClickable {
    void render(int mouseX, int mouseY, float partialTicks);

    void move(double x, double y);

    Point2D getPosition();

    boolean hitBox(double x, double y);

    List<String> getToolTip();

    void setEnabled(boolean enabled);

    boolean isEnabled();

    void setVisible(boolean visible);

    boolean isVisible();

    default void hide() {
        setVisible(false);
    }

    default void show() {
        setVisible(true);
    }

    default void enable() {
        setEnabled(true);
    }

    default void disable() {
        setEnabled(false);
    }

    default void enableAndShow() {
        enable();
        show();
    }

    default void disableAndHide() {
        disable();
        hide();
    }

    void setOnPressed(IPressable onPressed);

    void setOnReleased(IReleasable onReleased);

    void onPressed();

    void onReleased();

    @SideOnly(Side.CLIENT)
    interface IPressable {
        void onPressed(IClickable doThis);
    }

    @SideOnly(Side.CLIENT)
    interface IReleasable {
        void onReleased(IClickable doThis);
    }
}