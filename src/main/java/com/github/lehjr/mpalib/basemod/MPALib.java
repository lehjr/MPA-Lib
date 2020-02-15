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
package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.capabilities.player.CapabilityPlayerKeyStates;
import com.github.lehjr.mpalib.capabilities.render.ModelSpecNBTCapability;
import com.github.lehjr.mpalib.client.event.FOVUpdateEventHandler;
import com.github.lehjr.mpalib.client.event.RenderGameOverlayEventHandler;
import com.github.lehjr.forge.obj.MPALibOBJLoader;
import com.github.lehjr.mpalib.network.MPALibPackets;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(MPALIbConstants.MODID)
public class MPALib {
    public MPALib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MPALibConfig.COMMON_SPEC, MPALibConfig.setupConfigFile("mpalib-common.toml").getAbsolutePath());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MPALibConfig.CLIENT_SPEC, MPALibConfig.setupConfigFile("mpalib-client-only.toml").getAbsolutePath());

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the setupClient method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);

        // Register ourselves for server, registry and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        MPALibPackets.registerMPALibPackets();

        HeatCapability.register();

        // Modules
        PowerModuleCapability.register();
        ModelSpecNBTCapability.register();

        // Player
        CapabilityPlayerKeyStates.register();
    }

    private void setupClient(final FMLClientSetupEvent event) {
        ModelLoaderRegistry.registerLoader(MPALibOBJLoader.INSTANCE); // crashes if called in mod constructor
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof PlayerEntity)) {
            return;
        }
        event.addCapability(new ResourceLocation(MPALIbConstants.MODID, "player_keystates"), new CapabilityPlayerKeyStates());
    }
}