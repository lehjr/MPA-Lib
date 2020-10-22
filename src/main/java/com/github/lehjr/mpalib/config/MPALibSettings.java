package com.github.lehjr.mpalib.config;

import com.github.lehjr.mpalib.basemod.MPALIbConstants;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.IConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

public class MPALibSettings {
    public static final ClientConfig CLIENT_CONFIG;
    public static final ForgeConfigSpec CLIENT_SPEC;

    public static final ServerConfig SERVER_CONFIG;
    public static final ForgeConfigSpec SERVER_SPEC;

    static {
        {
            final Pair<ClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
            CLIENT_SPEC = clientSpecPair.getRight();
            CLIENT_CONFIG = clientSpecPair.getLeft();
        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_SPEC = serverSpecPair.getRight();
            SERVER_CONFIG = serverSpecPair.getLeft();
        }
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
    public static int chargingBaseMaxPower() {
        return getActiveConfig().map(config-> config.ARMOR_STAND_MAX_POWER.get()).orElse(10000);
    }

    static Optional<ServerConfig> getActiveConfig() {
        return Optional.ofNullable(SERVER_SPEC.isLoaded() ? SERVER_CONFIG : null);
    }

    /** Modules ----------------------------------------------------------------------------------- */
    private static volatile ModuleConfig moduleConfig;
    public static IConfig getModuleConfig() {
        if (moduleConfig == null) {
            synchronized (ModuleConfig.class) {
                if (moduleConfig == null) {
                    moduleConfig = new ModuleConfig(MPALIbConstants.MOD_ID);
                }
            }
        }
        return moduleConfig;
    }
}