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
