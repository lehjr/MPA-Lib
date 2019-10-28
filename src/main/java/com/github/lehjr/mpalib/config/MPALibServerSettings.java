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