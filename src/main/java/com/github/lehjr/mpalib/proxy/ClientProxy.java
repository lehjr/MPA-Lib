package com.github.lehjr.mpalib.proxy;

import com.github.lehjr.forge.obj.MPALibOBJLoader;
import com.github.lehjr.mpalib.client.event.FOVUpdateEventHandler;
import com.github.lehjr.mpalib.client.event.GameOverlayEventHandler;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 10/26/16.
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ModelLoaderRegistry.registerLoader(MPALibOBJLoader.INSTANCE);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        MinecraftForge.EVENT_BUS.register(new GameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
    }
}