package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.capabilities.player.CapabilityPlayerKeyStates;
import com.github.lehjr.mpalib.capabilities.render.ModelSpecNBTCapability;
import com.github.lehjr.mpalib.client.event.ArmorLayerSetup;
import com.github.lehjr.mpalib.client.event.FOVUpdateEventHandler;
import com.github.lehjr.mpalib.client.event.RenderGameOverlayEventHandler;
import com.github.lehjr.mpalib.client.gui.GuiIcon;
import com.github.lehjr.mpalib.client.gui.MPALibSpriteUploader;
import com.github.lehjr.mpalib.client.render.IconUtils;
import com.github.lehjr.mpalib.config.MPALibSettings;
import com.github.lehjr.mpalib.event.EventBusHelper;
import com.github.lehjr.mpalib.network.MPALibPackets;
import forge.MPAOBJLoader;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MPALIbConstants.MOD_ID)
public class MPALib {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public MPALib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MPALibSettings.CLIENT_SPEC, MPALibSettings.setupConfigFile("mpalib-client-only.toml", MPALIbConstants.MOD_ID).getAbsolutePath());
        // TODO:
//        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MPALibConfig.COMMON_SPEC, MPALibConfig.setupConfigFile("mpalib-common.toml").getAbsolutePath());
//        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MPALibConfig.SERVER_SPEC, "/lehjr/mpalib/mpalib-server.toml");

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        DistExecutor.runWhenOn(Dist.CLIENT, ()->()-> clientStart(FMLJavaModLoadingContext.get().getModEventBus()));
    }

    // Ripped from JEI
    private static void clientStart(IEventBus modEventBus) {
        if (Minecraft.getInstance() != null) {
            ModelLoaderRegistry.registerLoader(new ResourceLocation(MPALIbConstants.MOD_ID, "obj"), MPAOBJLoader.INSTANCE); // crashes if called in mod constructor
        }

        EventBusHelper.addListener(modEventBus, ColorHandlerEvent.Block.class, setupEvent -> {
            MPALibSpriteUploader spriteUploader = new MPALibSpriteUploader(Minecraft.getInstance().textureManager);
            GuiIcon icons = new GuiIcon(spriteUploader);
            IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            if (resourceManager instanceof IReloadableResourceManager) {
                IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
                reloadableResourceManager.addReloadListener(spriteUploader);
            }
            EventBusHelper.addLifecycleListener(modEventBus, FMLLoadCompleteEvent.class, loadCompleteEvent ->
                    IconUtils.setIconInstance(icons));
        });
    }

    @Mod.EventBusSubscriber(modid = MPALIbConstants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class MyStaticClientOnlyEventHandler {
        @SubscribeEvent
        public static void loadComplete(FMLLoadCompleteEvent evt) {
            ArmorLayerSetup.loadComplete(evt);
        }
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

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof PlayerEntity)) {
            return;
        }
        event.addCapability(new ResourceLocation(MPALIbConstants.MOD_ID, "player_keystates"), new CapabilityPlayerKeyStates());
    }
}
