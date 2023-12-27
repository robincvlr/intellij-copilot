package com.vllm.copilot.integrations.llms;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import com.vllm.copilot.enums.ModelServiceEnum;
import com.vllm.copilot.integrations.llms.hub.HubServiceProvider;
import com.vllm.copilot.settings.state.DevPilotLlmSettingsState;

@Service
public final class LlmProviderFactory {

    public LlmProvider getLlmProvider(Project project) {
        var settings = DevPilotLlmSettingsState.getInstance();
        String selectedModel = settings.getSelectedModel();
        ModelServiceEnum modelServiceEnum = ModelServiceEnum.fromName(selectedModel);

        switch (modelServiceEnum) {
            case LLAMA:
                return project.getService(HubServiceProvider.class);
        }
        return null;
    }

}
