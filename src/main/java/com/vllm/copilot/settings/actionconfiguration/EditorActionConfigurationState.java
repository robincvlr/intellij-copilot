package com.vllm.copilot.settings.actionconfiguration;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.vllm.copilot.enums.EditorActionEnum;

import java.util.LinkedHashMap;
import java.util.Map;

@State(
    name = "com.vllm.copilot.settings.actionconfiguration.EditorActionConfigurationState",
    storages = @Storage("DevPilotActionConfiguration.xml")
)
public class EditorActionConfigurationState implements PersistentStateComponent<EditorActionConfigurationState> {

    private final Map<String, String> defaultActions = new LinkedHashMap<>(Map.of(
        EditorActionEnum.PERFORMANCE_CHECK.getLabel(), EditorActionEnum.PERFORMANCE_CHECK.getPrompt(),
        EditorActionEnum.GENERATE_COMMENTS.getLabel(), EditorActionEnum.GENERATE_COMMENTS.getPrompt(),
        EditorActionEnum.GENERATE_TESTS.getLabel(), EditorActionEnum.GENERATE_TESTS.getPrompt(),
        EditorActionEnum.FIX_THIS.getLabel(), EditorActionEnum.FIX_THIS.getPrompt(),
        EditorActionEnum.EXPLAIN_THIS.getLabel(), EditorActionEnum.EXPLAIN_THIS.getPrompt(),
        EditorActionEnum.REVIEW_CODE.getLabel(), EditorActionEnum.REVIEW_CODE.getPrompt()
    ));

    public static EditorActionConfigurationState getInstance() {
        return ApplicationManager.getApplication().getService(EditorActionConfigurationState.class);
    }

    @Override
    public EditorActionConfigurationState getState() {
        return this;
    }

    @Override
    public void loadState(EditorActionConfigurationState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public Map<String, String> getDefaultActions() {
        return defaultActions;
    }

}
