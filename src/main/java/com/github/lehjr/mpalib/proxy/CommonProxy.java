package com.github.lehjr.mpalib.proxy;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.basemod.MPALib;
import com.github.lehjr.mpalib.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.capabilities.player.CapabilityPlayerKeyStates;
import com.github.lehjr.mpalib.event.PlayerTracker;
import com.github.lehjr.mpalib.network.MPALibPackets;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 10/26/16.
 */
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {
        MPALibPackets.registerNuminaPackets();
        HeatCapability.register();
        CapabilityPlayerKeyStates.register();
        MPALib.INSTANCE.configDir = new File(event.getModConfigurationDirectory(), MPALIbConstants.CONFIG_FOLDER);
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new PlayerTracker());
    }

    public void postInit(FMLPostInitializationEvent event) {
    }
}