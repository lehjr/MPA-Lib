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

package com.github.lehjr.mpalib.network;

import com.github.lehjr.mpalib.basemod.MPALibConstants;
import com.github.lehjr.mpalib.network.packets.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MPALibPackets {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL_INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MPALibConstants.MOD_ID, "data"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void registerMPALibPackets() {
        int i =0;

        CHANNEL_INSTANCE.registerMessage(
                i++,
                ModeChangeRequestPacket.class,
                ModeChangeRequestPacket::encode,
                ModeChangeRequestPacket::decode,
                ModeChangeRequestPacket::handle);

        CHANNEL_INSTANCE.registerMessage(
                i++,
                CosmeticInfoPacket.class,
                CosmeticInfoPacket::encode,
                CosmeticInfoPacket::decode,
                CosmeticInfoPacket::handle);

        CHANNEL_INSTANCE.registerMessage(
                i++,
                ToggleRequestPacket.class,
                ToggleRequestPacket::encode,
                ToggleRequestPacket::decode,
                ToggleRequestPacket::handle);

        CHANNEL_INSTANCE.registerMessage(
                i++,
                CreativeInstallModuleRequestPacket.class,
                CreativeInstallModuleRequestPacket::encode,
                CreativeInstallModuleRequestPacket::decode,
                CreativeInstallModuleRequestPacket::handle);

//        CHANNEL_INSTANCE.registerMessage(
//                i++,
//                MoveModuleFromSlotToSlotPacket.class,
//                MoveModuleFromSlotToSlotPacket::encode,
//                MoveModuleFromSlotToSlotPacket::decode,
//                MoveModuleFromSlotToSlotPacket::handle);

        CHANNEL_INSTANCE.registerMessage(
                i++,
                PlayerUpdatePacket.class,
                PlayerUpdatePacket::encode,
                PlayerUpdatePacket::decode,
                PlayerUpdatePacket::handle);

        CHANNEL_INSTANCE.registerMessage(
                i++,
                TweakRequestDoublePacket.class,
                TweakRequestDoublePacket::encode,
                TweakRequestDoublePacket::decode,
                TweakRequestDoublePacket::handle);
    }

    public SimpleChannel getWrapper() {
        return CHANNEL_INSTANCE;
    }
}