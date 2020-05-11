package com.github.lehjr.mpalib.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class MPALibSettings {
    public static final ClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec CLIENT_SPEC;
//
//    public static final CommonConfig COMMON_CONFIG;
//    public static final ForgeConfigSpec COMMON_SPEC;
//
//    public static final CommonConfig SERVER_CONFIG;
//    public static final ForgeConfigSpec SERVER_SPEC;


    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT_SPEC = clientSpecPair.getRight();
            CLIENT_CONFIG = clientSpecPair.getLeft();
        }
//        {
//            final Pair<CommonConfig, ForgeConfigSpec> commpnSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
//            COMMON_SPEC = commpnSpecPair.getRight();
//            COMMON_CONFIG = commpnSpecPair.getLeft();
//        }
//        {
//            final Pair<CommonConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
//            SERVER_SPEC = serverSpecPair.getRight();
//            SERVER_CONFIG = serverSpecPair.getLeft();
//        }
    }

    /** Client settings --------------------------------------------------------------------------- */
    public static boolean useFovFix() {
        return CLIENT_CONFIG != null ? CLIENT_CONFIG.USE_FOV_FIX.get() : true;
    }

    public static boolean useFovNormalize() {
        return CLIENT_CONFIG != null ? CLIENT_CONFIG.USE_FOV_NORMALIZE.get() : true;
    }

    public static boolean fovFixDefaultState() {
        return CLIENT_CONFIG != null ? CLIENT_CONFIG.FOV_FIX_DEAULT_STATE.get() : true;
    }

    public static boolean useSounds() {
        return CLIENT_CONFIG != null ? CLIENT_CONFIG.USE_SOUNDS.get() : true;
    }

    public static boolean enableDebugging() {
        return CLIENT_CONFIG != null ? CLIENT_CONFIG.DEBUGGING_INFO.get() : false;
    }

    /** Common/Server Settings -------------------------------------------------------------------- */
    // TODO

    public static File setupConfigFile(String fileName, String modId) {
        // MPALIbConstants.MOD_ID
        Path configFile = FMLPaths.CONFIGDIR.get().resolve("lehjr").resolve(modId).resolve(fileName);
        File cfgFile = configFile.toFile();
        try {
            Files.createDirectories(configFile.getParent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cfgFile;
    }
}
