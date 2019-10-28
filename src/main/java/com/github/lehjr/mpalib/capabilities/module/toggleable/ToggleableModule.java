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

package com.github.lehjr.mpalib.capabilities.module.toggleable;

import com.github.lehjr.mpalib.capabilities.IConfig;
import com.github.lehjr.mpalib.capabilities.module.powermodule.EnumModuleCategory;
import com.github.lehjr.mpalib.capabilities.module.powermodule.EnumModuleTarget;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModule;
import com.github.lehjr.mpalib.nbt.NBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;

public class ToggleableModule extends PowerModule implements IToggleableModule, INBTSerializable<NBTTagByte> {
    Boolean online;
    static final String TAG_ONLINE = "Active";
    static boolean defBool;
    public ToggleableModule(@Nonnull ItemStack module, EnumModuleCategory category, EnumModuleTarget target, IConfig config, boolean defToggleVal) {
        super(module, category, target, config);
        defBool = defToggleVal;
    }

//    @Override
//    public void updateFromNBT() {
//        online = MuseNBTUtils.getModuleBooleanOrSetDefault(module, TAG_ONLINE, defBool);
//    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = NBTUtils.getMuseModuleTag(module);
        if (nbt != null && nbt.hasKey(TAG_ONLINE, Constants.NBT.TAG_BYTE)) {
            deserializeNBT((NBTTagByte) nbt.getTag(TAG_ONLINE));
        } else {
            nbt.setBoolean(TAG_ONLINE, defBool);
            deserializeNBT(new NBTTagByte((byte) (defBool ? 1 : 0)));
        }
    }

    @Override
    public NBTTagByte serializeNBT() {
        return new NBTTagByte((byte) (online ? 1 : 0));
    }

    @Override
    public void deserializeNBT(NBTTagByte nbt) {
        online = nbt.getByte() == 1;
    }

    @Override
    public void toggleModule(boolean online) {
        this.online = online;
        NBTUtils.setModuleBoolean(module, TAG_ONLINE, online);
    }

    @Override
    public boolean isModuleOnline() {
        if (online == null)
            updateFromNBT();
        return online;
    }
}