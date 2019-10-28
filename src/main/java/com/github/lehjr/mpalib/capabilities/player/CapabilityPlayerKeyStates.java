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

package com.github.lehjr.mpalib.capabilities.player;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CapabilityPlayerKeyStates implements ICapabilitySerializable<NBTTagCompound> {
    @CapabilityInject(IPlayerKeyStates.class)
    public static Capability<IPlayerKeyStates> PLAYER_KEYSTATES = null;
    private IPlayerKeyStates instance = PLAYER_KEYSTATES.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerKeyStates.class, new Capability.IStorage<IPlayerKeyStates>() {
                    @Override
                    public NBTBase writeNBT(Capability<IPlayerKeyStates> capability, IPlayerKeyStates instance, EnumFacing side) {
                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setBoolean("jumpKey", instance.getJumpKeyState());
                        nbt.setBoolean("downKey", instance.getDownKeyState());
                        return nbt;
                    }

                    @Override
                    public void readNBT(Capability<IPlayerKeyStates> capability, IPlayerKeyStates instance, EnumFacing side, NBTBase nbt) {
                        if (nbt instanceof NBTTagCompound) {
                            instance.setJumpKeyState(((NBTTagCompound) nbt).getBoolean("jumpKey"));
                            instance.setJumpKeyState(((NBTTagCompound) nbt).getBoolean("downKey"));
                        }
                    }
                },
                () -> new PlayerKeyStateStorage());
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) PLAYER_KEYSTATES.getStorage().writeNBT(PLAYER_KEYSTATES, this.instance, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        PLAYER_KEYSTATES.getStorage().readNBT(PLAYER_KEYSTATES, this.instance, null, nbt);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_KEYSTATES;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == PLAYER_KEYSTATES ? (T) new PlayerKeyStateStorage() : null;
    }
}