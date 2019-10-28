package com.github.lehjr.mpalib.client.gui.frame;

import java.util.List;

public interface IGuiFrame {
    /**
     * @param mouseX
     * @param mouseY
     * @param button
     */
    void onMouseDown(double mouseX, double mouseY, int button);

    /**
     * @param mouseX
     * @param mouseY
     * @param button
     * @return true if mouse release is inside this frame and is handled here, else false
     */
    void onMouseUp(double mouseX, double mouseY, int button);

//    /**
//     * @param mouseX
//     * @param mouseY
//     * @param dWheel
//     * @return true if mouse pointer is inside this frame and scroll is handled here, else false
//     */
//    boolean mouseScrolled(double mouseX, double mouseY, double dWheel);

    /**
     * Fired when gui init is fired, during the creation phase and on resize. Can be used to setup the frame
     * including setting target dimensions.
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    void init(double left, double top, double right, double bottom);

    /**
     * Called in the render loop before rendering. Use to update this frame
     * @param mouseX
     * @param mouseY
     */
    void update(double mouseX, double mouseY);

    /**
     * Render elements of this frame. Ordering is important.
     * @param mouseX
     * @param mouseY
     * @param partialTicks
     */
    void render(int mouseX, int mouseY, float partialTicks);

    /**
     *
     * @param x mouseX
     * @param y mouseY
     * @return tooltip or null if not returning tooltip;
     */
    List<String> getToolTip(int x, int y);
}