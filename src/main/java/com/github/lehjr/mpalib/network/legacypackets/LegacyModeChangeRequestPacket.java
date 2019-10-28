package com.github.lehjr.mpalib.network.legacypackets;

import com.github.lehjr.mpalib.item.legacy.IModeChangingItem;
import com.github.lehjr.mpalib.module.legacy.IModuleManager;
import com.github.lehjr.mpalib.network.MuseByteBufferUtils;
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
        this.mode = MuseByteBufferUtils.readUTF8String(buf);
        this.slot = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        MuseByteBufferUtils.writeUTF8String(buf, mode);
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