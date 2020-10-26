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

package com.github.lehjr.mpalib.util.client.gui.clickable;

import com.github.lehjr.mpalib.util.client.gui.IconUtils;
import com.github.lehjr.mpalib.client.render.MPALibRenderState;
import com.github.lehjr.mpalib.util.client.render.MPALibRenderer;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.EnumModuleCategory;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.util.math.Colour;
import com.github.lehjr.mpalib.util.string.StringUtils;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Extends the Clickable class to make a clickable Augmentation; note that this
 * will not be an actual item.
 *
 * @author MachineMuse
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class ClickableModule extends Clickable {
    final Colour checkmarkcolour = new Colour(0.0F, 0.667F, 0.0F, 1.0F);
    boolean allowed = true;
    boolean installed = false;
    boolean isEnabled = true;
    boolean isVisible = true;
    ItemStack module;
    int inventorySlot;
    public final EnumModuleCategory category;

    public static final int offsetx = 8;
    public static final int offsety = 8;

    public ClickableModule(@Nonnull ItemStack module, Point2D position, int inventorySlot, EnumModuleCategory category) {
        super(position);
        this.module = module;
        this.inventorySlot = inventorySlot;
        this.category = category;
        allowed = module.getCapability(PowerModuleCapability.POWER_MODULE).map(pm->pm.isAllowed()).orElse(false);
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    @Override
    public List<ITextComponent> getToolTip() {
        List<ITextComponent> toolTipText = new ArrayList<>();
        toolTipText.add(getLocalizedName());
        toolTipText.addAll(StringUtils.wrapITextComponentToLength(getLocalizedDescription(), 30));
        return toolTipText;
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
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    @Override
    public boolean isVisible() {
        return isVisible;
    }

    public ITextComponent getLocalizedName() {
        if (this.getModule().isEmpty()) {
            return null;
        }
        return this.getModule().getDisplayName();
    }

    public ITextComponent getLocalizedDescription() {
        if (this.getModule().isEmpty()) {
            return null;
        }
        return new TranslationTextComponent(this.getModule().getTranslationKey().concat(".desc"));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks, float zLevel) {
        if (!getModule().isEmpty()) {
            MPALibRenderer.drawItemAt(getPosition().getX() - offsetx, getPosition().getY() - offsety, getModule());
            if (!allowed) {
                RenderSystem.pushMatrix();
                RenderSystem.translatef(0, 0, 250);
                MPALibRenderState.glowOn();
                String string = StringUtils.wrapMultipleFormatTags("X", StringUtils.FormatCodes.Bold, StringUtils.FormatCodes.DarkRed);
                MPALibRenderer.drawString(matrixStack, string, getPosition().getX() + 3, getPosition().getY() + 1);
                MPALibRenderState.glowOff();
                RenderSystem.popMatrix();
            } else if (installed) {
                RenderSystem.pushMatrix();
                RenderSystem.translatef(0, 0,250);
                MPALibRenderState.glowOn();
                IconUtils.getIcon().checkmark.draw(matrixStack, getPosition().getX() - offsetx + 1, getPosition().getY() - offsety + 1, checkmarkcolour.withAlpha(0.6F));
                MPALibRenderState.glowOff();
                RenderSystem.popMatrix();
            }
        }
    }

    @Override
    public boolean hitBox(double x, double y) {
        boolean hitx = Math.abs(x - getPosition().getX()) < 8;
        boolean hity = Math.abs(y - getPosition().getY()) < 8;
        return hitx && hity;
    }

    @Nonnull
    public ItemStack getModule() {
        return module;
    }

    public boolean equals(ClickableModule other) {
        return this.module == other.getModule();
    }

    public void setInstalled(boolean installed) {
        this.installed = installed;
    }

    public boolean isInstalled() {
        return installed;
    }
}