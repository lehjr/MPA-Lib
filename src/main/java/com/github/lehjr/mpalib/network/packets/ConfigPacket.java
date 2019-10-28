package com.github.lehjr.mpalib.network.packets;

import com.github.lehjr.mpalib.config.MPALibConfig;
import com.github.lehjr.mpalib.config.MPALibServerSettings;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ConfigPacket implements IMessage {
    MPALibServerSettings settings;
    public ConfigPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        settings = new MPALibServerSettings(buf);
        MPALibConfig.INSTANCE.setServerSettings(settings);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        settings = MPALibConfig.INSTANCE.getServerSettings();
        if (settings == null) {
            settings = new MPALibServerSettings();
            MPALibConfig.INSTANCE.setServerSettings(settings);
        }
        settings.writeToBuffer(buf);
    }

    public static class Handler implements IMessageHandler<ConfigPacket, IMessage> {
        @Override
        public IMessage onMessage(ConfigPacket message, MessageContext ctx) {
            return null;
        }
    }
}