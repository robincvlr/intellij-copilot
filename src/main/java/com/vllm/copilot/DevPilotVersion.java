package com.vllm.copilot;

import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.extensions.PluginId;

public class DevPilotVersion {
    public static String getDevPilotVersion() {
        var pluginId = PluginId.getId("com.vllm.copilot");
        var plugin = PluginManagerCore.getPlugin(pluginId);

        if (plugin != null) {
            return plugin.getVersion();
        }

        return null;
    }

    public static String getIdeaVersion() {
        return ApplicationInfo.getInstance().getFullVersion();
    }
}
