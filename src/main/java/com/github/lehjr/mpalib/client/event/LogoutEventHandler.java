package com.github.lehjr.mpalib.client.event;

import com.github.lehjr.mpalib.config.MPALibSettings;
import com.github.lehjr.mpalib.config.ModuleConfig;
import com.github.lehjr.mpalib.util.capabilities.module.powermodule.IConfig;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LogoutEventHandler {
    @SubscribeEvent
    public void onPlayerLogout(ClientPlayerNetworkEvent.LoggedInEvent event) {
        IConfig moduleConfig = MPALibSettings.getModuleConfig();
        if (moduleConfig instanceof ModuleConfig) {
            ((ModuleConfig) moduleConfig).writeMissingConfigValues();
        }
    }
}
