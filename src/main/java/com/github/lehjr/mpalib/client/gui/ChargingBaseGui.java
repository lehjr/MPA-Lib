package com.github.lehjr.mpalib.client.gui;

import com.github.lehjr.mpalib.container.ChargingBaseContainer;
import com.github.lehjr.mpalib.util.client.gui.ExtendedContainerScreen;
import com.github.lehjr.mpalib.util.client.gui.frame.InventoryFrame;
import com.github.lehjr.mpalib.util.client.gui.geometry.DrawableRelativeRect;
import com.github.lehjr.mpalib.util.client.gui.geometry.Point2D;
import com.github.lehjr.mpalib.util.client.gui.hud.meters.EnergyMeter;
import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class ChargingBaseGui extends ExtendedContainerScreen<ChargingBaseContainer> {
    /** The outer green rectangle */
    protected DrawableRelativeRect backgroundRect;
    protected InventoryFrame batterySlot, mainInventory, hotbar;
    final int slotWidth = 18;
    final int slotHeight = 18;
    int spacer = 7;
    EnergyMeter energyMeter;

    /**
        FIXME:
        battery disapears when putting into battery slot if only battery has energy
        ^ different behaviour when holding another battery in hand to swap
        meter location needs dynamic value
        basic battery energy bar disappears... needs z level adjustment..   

     */
    public ChargingBaseGui(ChargingBaseContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        backgroundRect = new DrawableRelativeRect(0, 0, 0, 0, true,
                new Colour(0.776F, 0.776F, 0.776F, 1F),
//                Colour.LIGHT_GREY,
                Colour.BLACK);
        energyMeter = new EnergyMeter();

        // slot 0
        batterySlot = new InventoryFrame(container,
                new Point2D(0,0), new Point2D(0, 0),
                getBlitOffset(),
                Colour.LIGHT_GREY, Colour.DARK_GREY, Colour.BLACK,
                1, 1, new ArrayList<Integer>(){{
            IntStream.range(0, 1).forEach(i-> add(i));
        }});
        frames.add(batterySlot);

        // slot 1-9
        mainInventory = new InventoryFrame(container,
                new Point2D(0,0), new Point2D(0, 0),
                getBlitOffset(),
                Colour.LIGHT_GREY, Colour.DARK_GREY, Colour.DARK_GREY,
                9, 3, new ArrayList<Integer>(){{
            IntStream.range(1, 28).forEach(i-> add(i));
        }});
        frames.add(mainInventory);

        // slot 28-37
        hotbar = new InventoryFrame(container,
                new Point2D(0,0), new Point2D(0, 0),
                getBlitOffset(),
                Colour.LIGHT_GREY, Colour.DARK_GREY, Colour.DARK_GREY,
                9, 1, new ArrayList<Integer>(){{
            IntStream.range(28, 37).forEach(i-> add(i));
        }});
        frames.add(hotbar);

        // add energy meter
    }

    Point2D getUlOffset () {
        return new Point2D(guiLeft + 8, guiTop + 8);
    }
    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
        backgroundRect.setTargetDimensions(new Point2D(getGuiLeft(), getGuiTop()), new Point2D(getXSize(), getYSize()));

        batterySlot.setUlShift(getUlOffset());
        batterySlot.init(
                backgroundRect.centerx() - (slotWidth * 0.5),
                backgroundRect.finalTop() + 30,
                backgroundRect.centerx() + (slotWidth * 0.5),
                backgroundRect.finalTop() + 30 + slotHeight);

        hotbar.setUlShift(getUlOffset());
        hotbar.init(
                backgroundRect.finalLeft() + spacer,
                backgroundRect.finalBottom() - spacer - slotHeight,
                backgroundRect.finalLeft() + spacer + 9 * slotWidth,
                backgroundRect.finalBottom() - spacer);


        mainInventory.setUlShift(getUlOffset());
        mainInventory.init(
                backgroundRect.finalLeft() + spacer,
                backgroundRect.finalBottom() - spacer - slotHeight - 3.25F - 3 * slotHeight,
                backgroundRect.finalLeft() + spacer + 9 * slotWidth,
                backgroundRect.finalBottom() - spacer - slotHeight - 3.25F);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        energyMeter.draw(matrixStack, (float) batterySlot.centerx() - 16,
                (float) (batterySlot.finalBottom() + spacer * 0.25),
                container.getEnergyForMeter(),
                getBlitOffset() + 2);
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(matrixStack, mouseX, mouseY);
        this.font.func_243248_b(matrixStack,  new TranslationTextComponent("mpalib.energy").appendString(": "), 32F, 50F, 4210752);
        String energyString = new StringBuilder().append(container.getEnergy()).append("/").append(container.getMaxEnergy()).append(" FE").toString();
        this.font.func_243248_b(matrixStack,
                new StringTextComponent(energyString),
                (float)((batterySlot.centerx() - font.getStringWidth(energyString) / 2) - guiLeft), // guiLeft here is important
                60F, 4210752);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        backgroundRect.draw(matrixStack, 1);
    }
}
