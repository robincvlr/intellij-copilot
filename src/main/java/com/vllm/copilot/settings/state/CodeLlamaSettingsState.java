package com.vllm.copilot.settings.state;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(name = "DevPilot_CodeLlamaSettings", storages = @Storage("DevPilot_CodeLlamaSettings.xml"))
public class CodeLlamaSettingsState implements PersistentStateComponent<CodeLlamaSettingsState> {
    private String modelHost;

    private String model;

    public static CodeLlamaSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(CodeLlamaSettingsState.class);
    }

    public String getModelHost() {
        return modelHost;
    }

    public void setModelHost(String modelHost) {
        this.modelHost = modelHost;
    }

    @Override
    public @Nullable CodeLlamaSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull CodeLlamaSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

}
