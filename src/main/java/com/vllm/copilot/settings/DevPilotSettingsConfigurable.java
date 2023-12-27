package com.vllm.copilot.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.util.NlsContexts;
import com.vllm.copilot.actions.editor.popupmenu.PopupMenuEditorActionGroupUtil;
import com.vllm.copilot.settings.state.CodeLlamaSettingsState;
import com.vllm.copilot.settings.state.DevPilotLlmSettingsState;
import com.vllm.copilot.settings.state.LanguageSettingsState;
import com.vllm.copilot.util.DevPilotMessageBundle;

import javax.swing.JComponent;

import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

public class DevPilotSettingsConfigurable implements Configurable, Disposable {

    private DevPilotSettingsComponent settingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return DevPilotMessageBundle.get("devpilot.settings");
    }

    @Override
    public @Nullable JComponent createComponent() {
        var settings = DevPilotLlmSettingsState.getInstance();
        settingsComponent = new DevPilotSettingsComponent(this, settings);
        return settingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        var settings = DevPilotLlmSettingsState.getInstance();
        var languageSettings = LanguageSettingsState.getInstance();
        var codeLlamaSettings = CodeLlamaSettingsState.getInstance();
        var serviceForm = settingsComponent.getDevPilotConfigForm();
        var selectedModel = serviceForm.getSelectedModel();

        return !settingsComponent.getFullName().equals(settings.getFullName())
                || !selectedModel.getName().equals(settings.getSelectedModel())
                || !serviceForm.getCodeLlamaBaseHost().equals(codeLlamaSettings.getModelHost())
                || !serviceForm.getCodeLlamaBaseModel().equals(codeLlamaSettings.getModel())
                || !serviceForm.getLanguageIndex().equals(languageSettings.getLanguageIndex());
    }

    @Override
    public void apply() throws ConfigurationException {
        var settings = DevPilotLlmSettingsState.getInstance();
        settings.setFullName(settingsComponent.getFullName());

        var languageSettings = LanguageSettingsState.getInstance();
        Integer languageIndex = settingsComponent.getLanguageIndex();
        languageSettings.setLanguageIndex(languageIndex);

        PopupMenuEditorActionGroupUtil.refreshActions(null);

        var codeLlamaSettings = CodeLlamaSettingsState.getInstance();
        var serviceForm = settingsComponent.getDevPilotConfigForm();
        var selectedModel = serviceForm.getSelectedModel();

        settings.setSelectedModel(selectedModel.getName());
        codeLlamaSettings.setModelHost(serviceForm.getCodeLlamaBaseHost());
        codeLlamaSettings.setModel(serviceForm.getCodeLlamaBaseModel());
    }

    @Override
    public void dispose() {
    }

}
