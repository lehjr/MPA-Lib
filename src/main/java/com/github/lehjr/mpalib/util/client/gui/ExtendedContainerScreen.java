package com.github.lehjr.mpalib.util.client.gui;

import com.github.lehjr.mpalib.util.client.gui.frame.IGuiFrame;
import com.github.lehjr.mpalib.util.client.gui.geometry.DrawableRect;
import com.github.lehjr.mpalib.util.math.Colour;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.List;

public class ExtendedContainerScreen<T extends Container> extends ContainerScreen<T> {
    protected long creationTime;
    protected DrawableRect tooltipRect;
    protected List<IGuiFrame> frames;

    public ExtendedContainerScreen(T screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        frames = new ArrayList();
        tooltipRect = new DrawableRect(
                0, 0, 0, 0,
                false,
                Colour.BLACK.withAlpha(0.9F),
                Colour.PURPLE);
    }

    @Override
    public void init(Minecraft minecraft, int width, int height) {
        super.init(minecraft, width, height);
    }

    @Override
    public void init() {
        super.init();
        minecraft.keyboardListener.enableRepeatEvents(true);
        creationTime = System.currentTimeMillis();
    }

    /**
     * Draws the gradient-rectangle background you see in the TinkerTable gui.
     */
    public void drawRectangularBackground() {

    }

    /**
     * Adds a frame to this gui's draw list.
     *
     * @param frame
     */
    public void addFrame(IGuiFrame frame) {
        frames.add(frame);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack) {
        super.renderBackground(matrixStack);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, int vOffset) {
        super.renderBackground(matrixStack, vOffset);
    }

    /**
     * Called every frame, draws the screen!
     */
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        update(mouseX, mouseY);
        renderFrames(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {

    }

    public void update(double x, double y) {
        for (IGuiFrame frame : frames) {
            frame.update(x, y);
        }
    }

    public void renderFrames(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for (IGuiFrame frame : frames) {
            frame.render(matrixStack, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dWheel) {
        for (IGuiFrame frame : frames) {
            if (frame.mouseScrolled(mouseX, mouseY, dWheel)) {
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, dWheel);
    }

    /**
     * Called when the mouse is clicked.
     */
    @Override
    public boolean mouseClicked(double x, double y, int button) {
        for (IGuiFrame frame : frames) {
            if(frame.mouseClicked(x, y, button)) {
                return true;
            }
        }
        if (super.mouseClicked(x, y, button)) {
            return true;
        }
        return false;
    }

    /**
     * Called when the mouse is moved or a mouse button is released. Signature:
     * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
     * mouseUp
     */
    @Override
    public boolean mouseReleased(double x, double y, int which) {
        for (IGuiFrame frame : frames) {
            frame.mouseReleased(x, y, which);
        }
        if (super.mouseReleased(x, y, which)) {
            return true;
        }
        return false;
    }

    public void drawToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        List<ITextComponent> tooltip = getToolTip(mouseX, mouseY);
        if (tooltip != null) {
            /*renderTooltip*/
            func_243308_b(matrixStack,tooltip, mouseX,mouseY);
        }
    }

    public List<ITextComponent> getToolTip(int x, int y) {
        List<ITextComponent> hitTip;
        for (IGuiFrame frame : frames) {
            hitTip = frame.getToolTip(x, y);
            if (hitTip != null) {
                return hitTip;
            }
        }
        return null;
    }

    public void setXSize(int xSize) {
        this.xSize = xSize;
        this.guiLeft = (this.width - getXSize()) / 2;
    }

    public void setYSize(int ySize) {
        this.ySize = ySize;
        this.guiTop = (this.height - getYSize()) / 2;
    }

    /**
     * Returns absolute screen coordinates (int 0 to width) from a relative
     * coordinate (float -1.0F to +1.0F)
     *
     * @param relx Relative X coordinate
     * @return Absolute X coordinate
     */
    public int absX(double relx) {
        int absx = (int) ((relx + 1) * getXSize() / 2);
        int xpadding = (width - getXSize()) / 2;
        return absx + xpadding;
    }

    /**
     * Returns relative coordinate (float -1.0F to +1.0F) from absolute
     * coordinates (int 0 to width)
     */
    public int relX(double absx) {
        int padding = (width - getXSize()) / 2;
        return (int) ((absx - padding) * 2 / getXSize() - 1);
    }

    /**
     * Returns absolute screen coordinates (int 0 to width) from a relative
     * coordinate (float -1.0F to +1.0F)
     *
     * @param rely Relative Y coordinate
     * @return Absolute Y coordinate
     */
    public int absY(double rely) {
        int absy = (int) ((rely + 1) * getYSize() / 2);
        int ypadding = (height - getYSize()) / 2;
        return absy + ypadding;
    }

    /**
     * Returns relative coordinate (float -1.0F to +1.0F) from absolute
     * coordinates (int 0 to width)
     */
    public int relY(float absy) {
        int padding = (height - getYSize()) / 2;
        return (int) ((absy - padding) * 2 / getYSize() - 1);
    }
}