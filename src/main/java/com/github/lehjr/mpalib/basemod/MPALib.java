package com.github.lehjr.mpalib.basemod;

import com.github.lehjr.mpalib.client.event.*;
import com.github.lehjr.mpalib.client.gui.ArmorStandGui;
import com.github.lehjr.mpalib.client.gui.ChargingBaseGui;
import com.github.lehjr.mpalib.util.client.render.IconUtils;
import com.github.lehjr.mpalib.config.ConfigHelper;
import com.github.lehjr.mpalib.config.MPALibSettings;
import com.github.lehjr.mpalib.entity.MPAArmorStandEntity;
import com.github.lehjr.mpalib.event.EventBusHelper;
import com.github.lehjr.mpalib.event.PlayerUpdateHandler;
import com.github.lehjr.mpalib.network.MPALibPackets;
import com.github.lehjr.mpalib.util.capabilities.heat.HeatCapability;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.PowerModuleCapability;
import com.github.lehjr.mpalib.util.capabilities.player.CapabilityPlayerKeyStates;
import com.github.lehjr.mpalib.util.capabilities.render.ModelSpecNBTCapability;
import com.github.lehjr.mpalib.util.client.gui.GuiIcon;
import com.github.lehjr.mpalib.util.client.gui.MPALibSpriteUploader;
import forge.MPAOBJLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(MPALIbConstants.MOD_ID)
public class MPALib {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public MPALib() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, MPALibSettings.CLIENT_SPEC, ConfigHelper.setupConfigFile("mpalib-client-only.toml", MPALIbConstants.MOD_ID).getAbsolutePath());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, MPALibSettings.SERVER_SPEC); // note config file location for dedicated server is stored in the world config

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();


        // Register the setup method for modloading
        modEventBus.addListener(this::setup);

        // Register the doClientStuff method for modloading
        modEventBus.addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);


        ModObjects.ITEMS.register(modEventBus);
        ModObjects.BLOCKS.register(modEventBus);
        ModObjects.TILE_TYPES.register(modEventBus);
        ModObjects.ENTITY_TYPES.register(modEventBus);
        ModObjects.CONTAINER_TYPES.register(modEventBus);

        DistExecutor.runWhenOn(Dist.CLIENT, ()->()-> clientStart(modEventBus));

        // handles loading and reloading event
        modEventBus.addListener((ModConfig.ModConfigEvent event) -> {
            new RuntimeException("Got config " + event.getConfig() + " name " + event.getConfig().getModId() + ":" + event.getConfig().getFileName());

            final ModConfig config = event.getConfig();
            if (config.getSpec() == MPALibSettings.SERVER_SPEC) {
                MPALibSettings.getModuleConfig().setServerConfig(config);
            }
        });
    }

    // Ripped from JEI
    private static void clientStart(IEventBus modEventBus) {
        if (Minecraft.getInstance() != null) {
            ModelLoaderRegistry.registerLoader(new ResourceLocation(MPALIbConstants.MOD_ID, "obj"), MPAOBJLoader.INSTANCE); // crashes if called in mod constructor
        }

        EventBusHelper.addListener(modEventBus, ColorHandlerEvent.Block.class, setupEvent -> {
            MPALibSpriteUploader iconUploader = new MPALibSpriteUploader(Minecraft.getInstance().textureManager, "gui");
            GuiIcon icons = new GuiIcon(iconUploader);
            IResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            if (resourceManager instanceof IReloadableResourceManager) {
                IReloadableResourceManager reloadableResourceManager = (IReloadableResourceManager) resourceManager;
                reloadableResourceManager.addReloadListener(iconUploader);
            }
            EventBusHelper.addLifecycleListener(modEventBus, FMLLoadCompleteEvent.class, loadCompleteEvent ->
                    IconUtils.setIconInstance(icons));
        });
    }

//    @Mod.EventBusSubscriber(modid = MPALIbConstants.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class MyStaticClientOnlyEventHandler {
//        @SubscribeEvent
//        public static void loadComplete(FMLLoadCompleteEvent evt) {
//            ArmorLayerSetup.loadComplete(evt);
//        }
//    }

    private void setup(final FMLCommonSetupEvent event) {
        MPALibPackets.registerMPALibPackets();

        HeatCapability.register();

        // Modules
        PowerModuleCapability.register();
        ModelSpecNBTCapability.register();

        // Player
        CapabilityPlayerKeyStates.register();

        MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler());

        DeferredWorkQueue.runLater(() -> {
            GlobalEntityTypeAttributes.put(ModObjects.ARMOR_WORKSTATION__ENTITY_TYPE.get(), MPAArmorStandEntity.setCustomAttributes().create());
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(new FOVUpdateEventHandler());
        MinecraftForge.EVENT_BUS.register(new RenderGameOverlayEventHandler());
        MinecraftForge.EVENT_BUS.register(new LogoutEventHandler());
        MinecraftForge.EVENT_BUS.register(new ToolTipEvent());

        ScreenManager.registerFactory(ModObjects.CHARGING_BASE_CONTAINER_TYPE.get(), ChargingBaseGui::new);
        ScreenManager.registerFactory(ModObjects.ARMOR_STAND_CONTAINER_TYPE.get(), ArmorStandGui::new);
    }

    @SubscribeEvent
    public void attachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof PlayerEntity)) {
            return;
        }
        event.addCapability(new ResourceLocation(MPALIbConstants.MOD_ID, "player_keystates"), new CapabilityPlayerKeyStates());
    }
}
