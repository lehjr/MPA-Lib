package com.github.lehjr.mpalib.network;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.legacy.network.LegacyModeChangeRequestPacket;
import com.github.lehjr.mpalib.network.packets.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MPALibPackets {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MPALIbConstants.MODID);

    public static void registerPackets() {
        int i = 0;

        // MPS/MPA
        INSTANCE.registerMessage(ConfigPacket.Handler.class, ConfigPacket.class, i++, Side.CLIENT);
        INSTANCE.registerMessage(PlayerUpdatePacket.Handler.class, PlayerUpdatePacket.class, i++, Side.SERVER);
        INSTANCE.registerMessage(ColourInfoPacket.Handler.class, ColourInfoPacket.class, i++, Side.SERVER);
        INSTANCE.registerMessage(CosmeticInfoPacket.Handler.class, CosmeticInfoPacket.class, i++, Side.SERVER);
        INSTANCE.registerMessage(CosmeticPresetPacket.Handler.class, CosmeticPresetPacket.class, i++, Side.SERVER);

        //MPA
        INSTANCE.registerMessage(ModeChangeRequestPacket.Handler.class, ModeChangeRequestPacket.class, i++, Side.SERVER);
        INSTANCE.registerMessage(ToggleRequestPacket.Handler.class, ToggleRequestPacket.class, i++, Side.SERVER);
        INSTANCE.registerMessage(TweakRequestDoublePacket.Handler.class, TweakRequestDoublePacket.class, i++, Side.SERVER);

        // Legacy MPS
        INSTANCE.registerMessage(LegacyModeChangeRequestPacket.Handler.class, LegacyModeChangeRequestPacket.class, i++, Side.SERVER);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAll(IMessage message) {
        INSTANCE.sendToAll(message);
    }

    public static void sendToAllAround(IMessage message, Entity entity, double d) {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, d));
    }

    public static void sendToDimension(IMessage message, int dim) {
        INSTANCE.sendToDimension(message, dim);
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}
