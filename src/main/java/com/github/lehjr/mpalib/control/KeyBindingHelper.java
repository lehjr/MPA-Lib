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

package com.github.lehjr.mpalib.control;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

/**
 * Created by leon on 7/4/16.
 */
public class KeyBindingHelper {
    static KeyBindingMap hash;

    static {
        new KeyBindingHelper();
    }

    static KeyBindingMap getKeyBindingMap() {
        try {
            if (hash == null) {
                if ((Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment"))
                    hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "HASH");
                else
                    hash = ReflectionHelper.getPrivateValue(KeyBinding.class, null, "HASH", "b", "field_74514_b");
            }
        } catch (Exception e) {
            return null;
        }
        return hash;
    }

    public boolean keyBindingHasKey(int key) {
        try {
            return (getKeyBindingMap() != null) ? (getKeyBindingMap().lookupActive(key) != null) : false;
        } catch (Exception ignored) {

        }
        return false;
    }

    public void removeKey(int key) {
        try {
            if (getKeyBindingMap() != null)
                hash.removeKey(hash.lookupActive(key));
        } catch (Exception ignored) {

        }
    }

    public void removeKey(KeyBinding key) {
        try {
            if (getKeyBindingMap() != null)
                hash.removeKey(key);
        } catch (Exception ignored) {

        }
    }
}