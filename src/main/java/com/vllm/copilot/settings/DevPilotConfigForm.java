package com.vllm.copilot.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import com.vllm.copilot.enums.ModelServiceEnum;
import com.vllm.copilot.util.DevPilotMessageBundle;
import com.vllm.copilot.settings.state.CodeLlamaSettingsState;
import com.vllm.copilot.settings.state.DevPilotLlmSettingsState;
import com.vllm.copilot.settings.state.LanguageSettingsState;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class DevPilotConfigForm {

    private final JPanel comboBoxPanel;

    private final ComboBox<ModelServiceEnum> modelComboBox;

    private final JPanel codeLlamaServicePanel;

    private final JBTextField codeLlamaBaseHostField;

    private final JBTextField codeLlamaBaseModelField;

    private Integer index;

    public DevPilotConfigForm() {
        var devPilotSettings = DevPilotLlmSettingsState.getInstance();

        var selectedModel = devPilotSettings.getSelectedModel();
        ModelServiceEnum selectedEnum = ModelServiceEnum.fromName(selectedModel);

        var codeLlamaSettings = CodeLlamaSettingsState.getInstance();
        codeLlamaBaseHostField = new JBTextField(codeLlamaSettings.getModelHost(), 30);
        codeLlamaBaseModelField = new JBTextField(codeLlamaSettings.getModel(), 30);
        codeLlamaServicePanel = createCodeLlamaServicePanel();

        panelShow(selectedEnum);

        var combo = new ComboBox<>(ModelServiceEnum.values());
        combo.setSelectedItem(selectedEnum);
        combo.addItemListener(e -> {
            var selected = (ModelServiceEnum) e.getItem();
            panelShow(selected);
        });

        modelComboBox = combo;
        comboBoxPanel = createCodeLlamaServicePanel();

        var instance = LanguageSettingsState.getInstance();
        index = instance.getLanguageIndex();
    }

    public JComponent getForm() {
        var form = FormBuilder.createFormBuilder()
            .addComponent(comboBoxPanel)
            .addComponent(codeLlamaServicePanel)
            .getPanel();
        form.setBorder(JBUI.Borders.emptyLeft(16));
        return form;
    }

    private JPanel createCodeLlamaServicePanel() {
        var panel = UI.PanelFactory.grid()
                .add(UI.PanelFactory.panel(codeLlamaBaseHostField)
                        .withLabel(DevPilotMessageBundle.get("devpilot.settings.service.modelHostLabel"))
                        .resizeX(false))
                .add(UI.PanelFactory.panel(codeLlamaBaseModelField)
                        .withLabel(DevPilotMessageBundle.get("devpilot.settings.service.modelLabel"))
                        .resizeX(false))
                .createPanel();
        panel.setBorder(JBUI.Borders.emptyLeft(16));
        return panel;
    }

    private void panelShow(ModelServiceEnum serviceEnum) {
        switch (serviceEnum) {
            case LLAMA:
                codeLlamaServicePanel.setVisible(false);
                break;
        }
    }

    public String getCodeLlamaBaseHost() {
        return codeLlamaBaseHostField.getText();
    }

    public String getCodeLlamaBaseModel() {
        return codeLlamaBaseModelField.getText();
    }

    public ModelServiceEnum getSelectedModel() {
        return (ModelServiceEnum) modelComboBox.getSelectedItem();
    }

    public Integer getLanguageIndex() {
        return index;
    }

}
