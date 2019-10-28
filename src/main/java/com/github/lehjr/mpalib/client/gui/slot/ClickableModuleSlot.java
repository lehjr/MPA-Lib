/*
 * Copyright (c) 2019 MachineMuse, Lehjr
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.lehjr.mpalib.client.gui.slot;

import com.github.lehjr.mpalib.capabilities.module.powermodule.EnumModuleCategory;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.client.gui.GuiIcons;
import com.github.lehjr.mpalib.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.math.Colour;
import com.github.lehjr.mpalib.string.StringUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * A wrapper for container slots with Item modules in them.
 * Note the 2 constructors:
 *      one for use as a module in the player's inventory
 *      the other is for use as a module in an ItemHandler Capability
 */
public class ClickableModuleSlot extends UniversalSlot implements IClickable {
    protected IPressable onPressed;
    protected IReleasable onReleased;
    boolean isVisible = true;
    boolean isEnabled = true;

    final Colour checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F);
    public static final int offsetx = 8;
    public static final int offsety = 8;

    boolean allowed = true;
    boolean installed = false;

    public ClickableModuleSlot(IInventory inventory, int index, int xPosition, int yPosition) {
        this(inventory, index, new Point2D(xPosition, yPosition));
    }

    public ClickableModuleSlot(IInventory inventory, int index, Point2D position) {
        super(inventory, index, (int)position.getX(), (int)position.getY());
    }

    public ClickableModuleSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        this(itemHandler, index, new Point2D(xPosition, yPosition));
    }

    public ClickableModuleSlot(IItemHandler itemHandler, int index, Point2D position) {
        super(itemHandler, index, (int)position.getX(), (int)position.getY());
    }

    @Override
    public void draw() {
        if (!getStack().isEmpty()) {
            Renderer.drawItemAt(getPosition().getX() - offsetx, getPosition().getY() - offsety, getStack());
            if (!allowed) {
                String string = StringUtils.wrapFormatTags("x", StringUtils.FormatCodes.DarkRed);
                Renderer.drawString(string, getPosition().getX() + 3, getPosition().getY() + 1);
            } else if (installed) {
                new GuiIcons.Checkmark(getPosition().getX() - offsetx + 1, getPosition().getY() - offsety + 1, checkmarkcolour, null, null, null, null);
            }
        }
    }

    @Override
    public void move(Point2D position) {
        this.position = position;
    }

    @Override
    public void move(double x, double y) {
        this.position.setX(x);
        this.position.setY(y);
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - position.getX()) < offsetx;
        boolean hity = Math.abs(y - position.getY()) < offsety;
        return hitx && hity;
    }

    @Override
    public List<String> getToolTip() {
        List<String> toolTipText = new ArrayList<>();
        toolTipText.add(getLocalizedName());
        toolTipText.addAll(Arrays.asList(StringUtils.wordUtilsWrap(getLocalizedDescription(), 30)));
        return toolTipText;
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && this.isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.isEnabled = enabled;
    }

    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    @Override
    public void onReleased() {
        if (this.isVisible && this.isEnabled && this.onReleased != null) {
            this.onReleased.onReleased(this);
        }
    }

    @Override
    public void onPressed() {
        if (this.isVisible && this.isEnabled && this.onPressed != null) {
            this.onPressed.onPressed(this);
        }
    }

    public String getLocalizedName() {
        if (this.getStack().isEmpty())
            return null;
        return this.getStack().getDisplayName();
    }

    public String getLocalizedDescription() {
        if (this.getStack().isEmpty())
            return null;
        return new TextComponentTranslation(this.getStack().getTranslationKey().concat(".desc")).getFormattedText();
    }

    public EnumModuleCategory getCategory() {
        return Optional.of(getStack().getCapability(PowerModuleCapability.POWER_MODULE, null)).map(m->m.getCategory()).orElse(EnumModuleCategory.NONE);
    }

    public boolean equals(ClickableModuleSlot other) {
        return this.getStack().isItemEqual(other.getStack());
    }

    public void setAllowed(boolean allowed) {
        this.allowed = allowed;
    }

    public boolean isAllowed() {
        return this.allowed;
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public boolean isInstalled() {
        return installed;
    }

    @Override
    public void setOnPressed(IPressable onPressed) {
        this.onPressed = onPressed;
    }

    @Override
    public void setOnReleased(IReleasable onReleased) {
        this.onReleased = onReleased;
    }
}