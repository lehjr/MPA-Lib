//package com.github.lehjr.mpalib.network.legacypackets;
//
//import com.github.lehjr.mpalib.network.MuseByteBufferUtils;
//import io.netty.buffer.ByteBuf;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//import net.minecraftforge.fml.relauncher.Side;
// FIXME: needs a way to get the intance of the IModuleManager Implementation
//public class LegacyToggleRequestPacket implements IMessage {
//    EntityPlayer player;
//    String module;
//    Boolean active;
//
//    public LegacyToggleRequestPacket() {
//
//    }
//
//    public LegacyToggleRequestPacket(EntityPlayer player, String module, Boolean active) {
//        this.player = player;
//        this.module = module;
//        this.active = active;
//    }
//
//    @Override
//    public void fromBytes(ByteBuf buf) {
//        this.module = MuseByteBufferUtils.readUTF8String(buf);
//        this.active = buf.readBoolean();
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf) {
//        MuseByteBufferUtils.writeUTF8String(buf, module);
//        buf.writeBoolean(active);
//    }
//
//    public static class Handler implements IMessageHandler<LegacyToggleRequestPacket,IMessage> {
//        @Override
//        public IMessage onMessage(LegacyToggleRequestPacket message, MessageContext ctx) {
//            if (ctx.side == Side.SERVER) {
//                final EntityPlayerMP player = ctx.getServerHandler().player;
//                player.getServerWorld().addScheduledTask(() -> {
//                    String module = message.module;
//                    Boolean active = message.active;
//                    ModuleManager.INSTANCE.toggleModuleForPlayer(player, module, active);
//                });
//            }
//            return null;
//        }
//    }
//}
