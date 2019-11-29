/*
 * MPA-Lib (Formerly known as Numina)
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

package com.github.lehjr.mpalib.legacy.network;

import com.github.lehjr.mpalib.legacy.item.IModeChangingItem;
import com.github.lehjr.mpalib.legacy.module.IModuleManager;
import com.github.lehjr.mpalib.network.MPALibByteBufferUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class LegacyModeChangeRequestPacket implements IMessage {
    protected String mode;
    protected int slot;

    public LegacyModeChangeRequestPacket(String mode, int slot) {
        this.mode = mode;
        this.slot = slot;
    }

    public LegacyModeChangeRequestPacket() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mode = MPALibByteBufferUtils.readUTF8String(buf);
        this.slot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MPALibByteBufferUtils.writeUTF8String(buf, mode);
        buf.writeInt(slot);
    }

    public static class Handler implements IMessageHandler<LegacyModeChangeRequestPacket, IMessage> {
        @Override
        public IMessage onMessage(LegacyModeChangeRequestPacket message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int slot = message.slot;
                    String mode = message.mode;
                    if (slot > -1 && slot < 9) {
                        ItemStack stack = player.inventory.mainInventory.get(slot);
                        if (!stack.isEmpty() && stack.getItem() instanceof IModeChangingItem) {
                            IModeChangingItem modeChangingItem = ((IModeChangingItem) stack.getItem());
                            IModuleManager moduleManager = modeChangingItem.getModuleManager();
                            if (moduleManager.isValidForItem(stack, mode))
                                modeChangingItem.setActiveMode(stack, mode);
                        }
                    }
                });
            }
            return null;
        }
    }
}