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

package com.github.lehjr.mpalib.client.sound;

import com.github.lehjr.mpalib.basemod.MPALibLogger;
import com.github.lehjr.mpalib.config.MPALibConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

/**
 * Created by Claire on 8/27/2015.
 * <p>
 * Ported to Java by lehjr on 10/22/16.
 */
@SideOnly(Side.CLIENT)
public class Musique {
    private static HashMap<String, MovingSoundPlayer> soundMap = new HashMap<>();

    public static SoundHandler mcsound() {
        return Minecraft.getMinecraft().getSoundHandler();
    }

    public static void playClientSound(SoundEvent soundEvt, SoundCategory categoryIn, float volumeIn, BlockPos posIn) {
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && MPALibConfig.useSounds()) {
            BlockPos pos = (posIn != null) ? posIn : Minecraft.getMinecraft().player.getPosition();

            // creates a sound
            PositionedSoundRecord sound = new PositionedSoundRecord(soundEvt, categoryIn, volumeIn, 1.0F, pos);
            mcsound().playSound(sound);
        }
    }

    public static String makeSoundString(EntityPlayer player, SoundEvent soundEvt) {
                return makeSoundString(player, soundEvt.getSoundName());
    }

    public static String makeSoundString(EntityPlayer player, ResourceLocation soundname) {
        return player.getUniqueID().toString() + soundname;
    }

    public static void playerSound(EntityPlayer player, ResourceLocation location, SoundCategory categoryIn, float volume, Float pitch, Boolean continuous) {
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(location);
        if (soundEvent != null) {
//            System.out.println(soundEvent.getSoundName().toString());
            playerSound(player, soundEvent, categoryIn, volume, pitch, continuous);
        } else {
            soundEvent = new SoundEvent(location);
            if (soundEvent != null)
                playerSound(player, soundEvent, categoryIn, volume, pitch, continuous);
            else
                MPALibLogger.logError("Sound event not found for " + location.toString());
        }
    }

    public static void playerSound(EntityPlayer player, SoundEvent soundEvt, SoundCategory categoryIn, float volume, Float pitch, Boolean continuous) {
        pitch = (pitch != null) ? pitch : 1.0F;
        continuous = (continuous != null) ? continuous : true;
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && MPALibConfig.useSounds() && soundEvt != null) {
            String soundID = makeSoundString(player, soundEvt);
            MovingSoundPlayer sound = soundMap.get(soundID);

            if (sound != null && (sound.isDonePlaying() || !sound.canRepeat())) {
                stopPlayerSound(player, soundEvt);
                sound = null;
            }
            if (sound != null) {
                sound.updateVolume(volume).updatePitch(pitch).updateRepeat(continuous);
            } else {
//                MPALibLogger.logDebug("New sound: " + soundEvt.getSoundName());
                MovingSoundPlayer newsound = new MovingSoundPlayer(soundEvt, categoryIn, player, volume * 2.0f, pitch, continuous);
                mcsound().playSound(newsound);
                soundMap.put(soundID, newsound);
            }
        }
    }

    public static void stopPlayerSound(EntityPlayer player, ResourceLocation location) {
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(location);
        if (soundEvent != null) {
//            System.out.println(soundEvent.getSoundName().toString());
            stopPlayerSound(player, soundEvent);
        } else {
            soundEvent = new SoundEvent(location);
            if (soundEvent != null)
                stopPlayerSound(player, soundEvent);
            else
                MPALibLogger.logError("Sound event not found for " + location.toString());
        }
    }

    public static void stopPlayerSound(EntityPlayer player, SoundEvent soundEvt) {
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && MPALibConfig.useSounds()) {
            String soundID = makeSoundString(player, soundEvt);
            MovingSoundPlayer sound = soundMap.get(soundID);
            if (sound != null) {
                sound.stopPlaying();
                mcsound().stopSound(sound);
            }
            soundMap.remove(soundID);
//             MPALibLogger.logDebug("Sound stopped: " + soundEvt.getSoundName());
        }
    }

    public GameSettings options() {
        return Minecraft.getMinecraft().gameSettings;
    }
}