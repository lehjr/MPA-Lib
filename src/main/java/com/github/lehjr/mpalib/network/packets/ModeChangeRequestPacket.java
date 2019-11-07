package com.github.lehjr.mpalib.network.packets;

import com.github.lehjr.mpalib.capabilities.inventory.modechanging.IModeChangingItem;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.Optional;

public class ModeChangeRequestPacket implements IMessage {
    protected int mode;
    protected int slot;

    public ModeChangeRequestPacket(int mode, int slot) {
        this.mode = mode;
        this.slot = slot;
    }

    public ModeChangeRequestPacket() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.mode = buf.readInt();
        this.slot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(mode);
        buf.writeInt(slot);
    }

    public static class Handler implements IMessageHandler<ModeChangeRequestPacket, IMessage> {
        @Override
        public IMessage onMessage(ModeChangeRequestPacket message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                EntityPlayerMP player = ctx.getServerHandler().player;
                player.getServerWorld().addScheduledTask(() -> {
                    int slot = message.slot;
                    int mode = message.mode;
                    if (slot > -1 && slot < 9) {
                        Optional.ofNullable(player.inventory.mainInventory.get(slot).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
                                .ifPresent(handler -> {
                                    if (handler instanceof IModeChangingItem)
                                        ((IModeChangingItem) handler).setActiveMode(mode);
                                });
                    }
                });
            }
            return null;
        }
    }
}