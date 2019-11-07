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

package com.github.lehjr.mpalib.network.packets;

import com.github.lehjr.mpalib.capabilities.inventory.modularitem.IModularItem;
import com.github.lehjr.mpalib.network.MuseByteBufferUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;

public class TweakRequestDoublePacket implements IMessage {
    protected int itemSlot;
    protected ResourceLocation moduleName;
    protected String tweakName;
    protected double tweakValue;

    public TweakRequestDoublePacket() {

    }

    public TweakRequestDoublePacket(int itemSlot, ResourceLocation moduleRegName, String tweakName, double tweakValue) {
        this.itemSlot = itemSlot;
        this.moduleName = moduleRegName;
        this.tweakName = tweakName;
        this.tweakValue = tweakValue;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.itemSlot = buf.readInt();
        this.moduleName = new ResourceLocation(MuseByteBufferUtils.readUTF8String(buf));
        this.tweakName = MuseByteBufferUtils.readUTF8String(buf);
        this.tweakValue = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(itemSlot);
        MuseByteBufferUtils.writeUTF8String(buf, moduleName.toString());
        MuseByteBufferUtils.writeUTF8String(buf, tweakName);
        buf.writeDouble(tweakValue);
    }

    public static class Handler implements IMessageHandler<TweakRequestDoublePacket, IMessage> {
        @Override
        public IMessage onMessage(TweakRequestDoublePacket message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                final EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int itemSlot = message.itemSlot;
                    ResourceLocation moduleName = message.moduleName;
                    String tweakName = message.tweakName;
                    double tweakValue = message.tweakValue;

                    if (moduleName != null && tweakName != null) {
                        ItemStack stack = player.inventory.getStackInSlot(itemSlot);
                        Optional.ofNullable(stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)).ifPresent(iItemHandler -> {
                            if (iItemHandler instanceof IModularItem) {
                                ((IModularItem) iItemHandler).setModuleTweakDouble(moduleName, tweakName, tweakValue);
                            }
                        });
                    }
                });
            }
            return null;
        }
    }
}