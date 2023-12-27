package com.vllm.copilot.util;

import com.vllm.copilot.DevPilotVersion;
import com.vllm.copilot.settings.state.DevPilotLlmSettingsState;

public class UserAgentUtils {
    public static String getUserAgent() {
        // format: idea version|plugin version|uuid
        return String.format("%s|%s|%s", DevPilotVersion.getIdeaVersion(),
                DevPilotVersion.getDevPilotVersion(), DevPilotLlmSettingsState.getInstance().getUuid());
    }
}
