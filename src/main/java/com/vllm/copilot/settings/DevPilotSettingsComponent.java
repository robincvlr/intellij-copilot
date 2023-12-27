package com.vllm.copilot.settings;

import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import com.vllm.copilot.settings.state.DevPilotLlmSettingsState;
import com.vllm.copilot.util.DevPilotMessageBundle;

import javax.swing.JPanel;

public class DevPilotSettingsComponent {

    private final JPanel mainPanel;

    private final JBTextField fullNameField;

    private final DevPilotConfigForm devPilotConfigForm;

    public DevPilotSettingsComponent(DevPilotSettingsConfigurable devPilotSettingsConfigurable, DevPilotLlmSettingsState settings) {
        devPilotConfigForm = new DevPilotConfigForm();

        fullNameField = new JBTextField(settings.getFullName(), 20);

        mainPanel = FormBuilder.createFormBuilder()
            .addComponent(UI.PanelFactory.panel(fullNameField)
                .withLabel(DevPilotMessageBundle.get("devpilot.setting.displayNameFieldLabel"))
                .resizeX(false)
                .createPanel())
            .addComponent(new TitledSeparator(DevPilotMessageBundle.get("devpilot.settings.service.title")))
            .addComponent(devPilotConfigForm.getForm())
            .addVerticalGap(8)
            .addComponentFillVertically(new JPanel(), 0)
            .getPanel();
    }

    public JPanel getPanel() {
        return mainPanel;
    }

    public DevPilotConfigForm getDevPilotConfigForm() {
        return devPilotConfigForm;
    }

    // Getting the full name from the settings
    public String getFullName() {
        return fullNameField.getText();
    }

    public Integer getLanguageIndex() {
        return devPilotConfigForm.getLanguageIndex();
    }
}
