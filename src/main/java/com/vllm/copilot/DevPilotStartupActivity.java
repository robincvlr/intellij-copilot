package com.vllm.copilot;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.vllm.copilot.actions.editor.popupmenu.PopupMenuEditorActionGroupUtil;

import org.jetbrains.annotations.NotNull;

public class DevPilotStartupActivity implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {
        PopupMenuEditorActionGroupUtil.refreshActions(project);
    }

}
