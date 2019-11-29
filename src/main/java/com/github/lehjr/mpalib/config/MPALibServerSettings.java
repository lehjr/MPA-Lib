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

package com.github.lehjr.mpalib.config;

import io.netty.buffer.ByteBuf;

public class MPALibServerSettings {
    public final double mekRatio;
    public final double ic2Ratio;
    public final double rsRatio;
    public final double ae2Ratio;

    public final int maxTier1;
    public final int maxTier2;
    public final int maxTier3;
    public final int maxTier4;

    /**
     * Server side initialization
     */
    public MPALibServerSettings() {
        mekRatio = MPALibSettings.general.mekRatio;
        ic2Ratio = MPALibSettings.general.ic2Ratio;
        rsRatio = MPALibSettings.general.rsRatio;
        ae2Ratio = MPALibSettings.general.ae2Ratio;

        maxTier1 = MPALibSettings.general.maxTier1;
        maxTier2 = MPALibSettings.general.maxTier2;
        maxTier3 = MPALibSettings.general.maxTier3;
        maxTier4 = MPALibSettings.general.maxTier4;
    }

    /**
     * Set values from packet sent from server
     */
    public MPALibServerSettings(final ByteBuf buf) {
        mekRatio = buf.readDouble();
        ic2Ratio = buf.readDouble();
        rsRatio = buf.readDouble();
        ae2Ratio = buf.readDouble();

        maxTier1 = buf.readInt();
        maxTier2 = buf.readInt();
        maxTier3 = buf.readInt();
        maxTier4 = buf.readInt();
    }

    /**
     * This is a server side operation that gets the values and writes them to the packet.
     * This packet is then sent to a new client on login to sync config values. This allows
     * the server to be able to control these settings.
     * @param buf
     */
    public void writeToBuffer(final ByteBuf buf) {
        buf.writeDouble(mekRatio);
        buf.writeDouble(ic2Ratio);
        buf.writeDouble(rsRatio);
        buf.writeDouble(ae2Ratio);

        buf.writeInt(maxTier1);
        buf.writeInt(maxTier2);
        buf.writeInt(maxTier3);
        buf.writeInt(maxTier4);
    }
}