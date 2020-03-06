package com.github.lehjr.mpalib.client.sound;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MPALIbConstants.MOD_ID, value = Dist.CLIENT)
public class SoundDictionary {
    public static SoundEvent SOUND_EVENT_GUI_INSTALL = registerSound("gui_install");
    public static SoundEvent SOUND_EVENT_GUI_SELECT = registerSound("gui_select");
    public static SoundEvent SOUND_EVENT_BOOP = registerSound("boop");

    static {
        new SoundDictionary();
    }

    @SubscribeEvent
    public static void registerSoundEvent(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                SOUND_EVENT_GUI_INSTALL,
                SOUND_EVENT_GUI_SELECT,
                SOUND_EVENT_BOOP);
    }

    private static SoundEvent registerSound(String soundName) {
        ResourceLocation location = new ResourceLocation(MPALIbConstants.MOD_ID, soundName);
        SoundEvent event = new SoundEvent(location).setRegistryName(location);
        return event;
    }
}