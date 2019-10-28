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

package com.github.lehjr.mpalib.client.event;

import com.github.lehjr.mpalib.capabilities.inventory.modechanging.IModeChangingItem;
import com.github.lehjr.mpalib.client.render.IconUtils;
import com.github.lehjr.mpalib.client.render.RenderState;
import com.github.lehjr.mpalib.client.render.Renderer;
import com.github.lehjr.mpalib.client.render.TextureUtils;
import com.github.lehjr.mpalib.math.Colour;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ISpecialArmor;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:17 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
@SideOnly(Side.CLIENT)
public class GameOverlayEventHandler {

    static {
        new GameOverlayEventHandler();
    }

    @SubscribeEvent
    public void onPostRenderGameOverlayEvent(final RenderGameOverlayEvent.Post e) {
        if (e.getType().equals(RenderGameOverlayEvent.ElementType.HOTBAR)) {
            drawModeChangeIcons();
        }
    }

    public void drawModeChangeIcons() {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.player;
        int i = player.inventory.currentItem;
        ItemStack stack = player.inventory.getCurrentItem();

        if (!stack.isEmpty() && stack.getItem() instanceof com.github.lehjr.mpalib.item.legacy.IModeChangingItem) {
            com.github.lehjr.mpalib.item.legacy.IModeChangingItem item = (com.github.lehjr.mpalib.item.legacy.IModeChangingItem) (stack.getItem());
            ScaledResolution screen = new ScaledResolution(mc);
            TextureUtils.pushTexture(TextureUtils.TEXTURE_QUILT);
            RenderState.blendingOn();
            TextureAtlasSprite currentMode = item.getModeIcon(item.getActiveMode(stack), stack, player);
            double currX;
            double currY;
            int sw = screen.getScaledWidth();
            int sh = screen.getScaledHeight();
            int baroffset = 22;

            // FIXME: multiple row fix needed?

            if (!player.capabilities.isCreativeMode) {
                baroffset += 16;
                if (ForgeHooks.getTotalArmorValue(player) > 0) {
                    baroffset += 8;
                }
            }
            RenderState.scissorsOn(0, 0, sw, sh - baroffset);
            baroffset = screen.getScaledHeight() - baroffset;
            currX = sw / 2.0 - 89.0 + 20.0 * i;
            currY = baroffset - 18;
            if (currentMode != null) {
                IconUtils.drawIconAt(currX, currY, currentMode, Colour.WHITE.withAlpha(0.8));
            }
            RenderState.scissorsOff();
            RenderState.blendingOff();
            TextureUtils.popTexture();
            Colour.WHITE.doGL();
        } else {
            Optional.of(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(handler->{
                if (handler instanceof IModeChangingItem) {
                    ItemStack module = ((IModeChangingItem) handler).getActiveModule();
                    if (!module.isEmpty()) {
                        ScaledResolution screen = new ScaledResolution(mc);
                        TextureUtils.pushTexture(TextureUtils.TEXTURE_QUILT);
                        RenderState.blendingOn();
                        double currX;
                        double currY;
                        int sw = screen.getScaledWidth();
                        int sh = screen.getScaledHeight();
                        int baroffset = 22;
                        if (!player.capabilities.isCreativeMode) {
                            baroffset += 16;

                            if(player.getTotalArmorValue() > 8) {
                                baroffset += 8;
                            }

                            if (ForgeHooks.getTotalArmorValue(player) > 0) { // FIXME
                                baroffset += 8;
                            }
                        }
                        RenderState.scissorsOn(0, 0, sw, sh - baroffset);
                        baroffset = screen.getScaledHeight() - baroffset;
                        currX = sw / 2.0 - 89.0 + 20.0 * i;
                        currY = baroffset - 18;
                        Renderer.drawItemAt(currX, currY, module);
                        RenderState.scissorsOff();
                        RenderState.blendingOff();
                        TextureUtils.popTexture();
                        Colour.WHITE.doGL();
                    }
                }
            });
        }
    }

    public static int getTotalArmorValue(EntityPlayer player) {
        int ret = player.getTotalArmorValue();
        for (int x = 0; x < player.inventory.armorInventory.size(); x++) {
            ItemStack stack = player.inventory.armorInventory.get(x);
            if (stack.getItem() instanceof ISpecialArmor) {
                ret += ((ISpecialArmor)stack.getItem()).getArmorDisplay(player, stack, x);
            }
        }
        return ret;
    }
}