package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.capabilities.player.CapabilityPlayerKeyStates;
import com.github.lehjr.mpalib.capabilities.render.ModelSpecNBTCapability;
import com.github.lehjr.mpalib.client.event.FOVUpdateEventHandler;
import com.github.lehjr.mpalib.client.event.RenderGameOverlayEventHandler;
import com.github.lehjr.mpalib.client.gui.GuiIcon;
import com.github.lehjr.mpalib.client.gui.MPALibSpriteUploader;
import com.github.lehjr.mpalib.client.render.IconUtils;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, MPALibConfig.COMMON_SPEC, MPALibConfig.setupConfigFile("mpalib-common.toml").getAbsolutePath());
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MPALibConfig.CLIENT_SPEC, MPALibConfig.setupConfigFile("mpalib-client-only.toml").getAbsolutePath());

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        // Register the processIMC method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
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

    private void setup(final FMLCommonSetupEvent event) {
        MPALibPackets.registerMPALibPackets();

        HeatCapability.register();

        // Modules
        PowerModuleCapability.register();
        ModelSpecNBTCapability.register();

        // Player
        CapabilityPlayerKeyStates.register();


        //

        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
//        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);

//        ModelLoaderRegistry.registerLoader(MPALibOBJLoader.INSTANCE); // crashes if called in mod constructor
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
    }


    private void enqueueIMC(final InterModEnqueueEvent event) {
        // some example code to dispatch IMC to another mod
        InterModComms.sendTo("examplemod", "helloworld", () -> {
            LOGGER.info("Hello world from the MDK");
            return "Hello world";
        });
    }

    private void processIMC(final InterModProcessEvent event) {
        // some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m -> m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
            // register a new block here
            LOGGER.info("HELLO from Register Block");
        }
    }



    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof PlayerEntity)) {
            return;
        }
        event.addCapability(new ResourceLocation(MPALIbConstants.MOD_ID, "player_keystates"), new CapabilityPlayerKeyStates());
    }
}
