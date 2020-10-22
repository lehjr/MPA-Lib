package com.github.lehjr.mpalib.util.client.gui.clickable;

import com.github.lehjr.mpalib.util.client.render.MPALibRenderer;
import com.github.lehjr.mpalib.util.client.gui.geometry.DrawableTile;
import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

public class CheckBox extends Clickable {
    protected boolean isChecked;
    protected DrawableTile tile;

    String label;

    public CheckBox(Point2D position, String displayString, boolean isChecked){
        Point2D ul = position.plus(4, 4);
        this.tile = new DrawableTile(ul, ul.plus(8, 8)).setBackgroundColour(Colour.BLACK)
                .setTopBorderColour(Colour.DARK_GREY).setBottomBorderColour(Colour.DARK_GREY);
        this.label = displayString;
        this.isChecked = isChecked;
        this.enableAndShow();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float zLevel) {
        if(this.isVisible) {
            tile.draw(matrixStack, zLevel);
            if (isChecked) {
                MPALibRenderer.drawString(matrixStack, "x", tile.centerx() - 2, tile.centery() - 5, Colour.WHITE);
            }
            MPALibRenderer.drawString(matrixStack, label, tile.centerx() + 8, tile.centery() - 4, Colour.WHITE);
        }
    }

    @Override
    public boolean hitBox(double x, double y) {
        if (this.isVisible() && this.isEnabled()) {
            return tile.containsPoint(x, y);
        } else {
            return false;
        }
    }

    @Override
    public void setPosition(Point2D position) {
        super.setPosition(position);
        tile.setPosition(position);
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
    public void onPressed() {
        if (this.isVisible() && this.isEnabled()) {
            Minecraft.getInstance().getSoundHandler().play(SimpleSound.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            this.isChecked = !this.isChecked;
        }
        super.onPressed();
    }
}