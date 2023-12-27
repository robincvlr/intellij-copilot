package com.vllm.copilot.actions.toolbar;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.vllm.copilot.DevPilotIcons;
import com.vllm.copilot.gui.toolwindows.DevPilotChatToolWindowFactory;
import com.vllm.copilot.util.DevPilotMessageBundle;

import org.jetbrains.annotations.NotNull;

public class ToolbarClearAction extends AnAction {
    public ToolbarClearAction() {
        super(DevPilotMessageBundle.get("devpilot.toolbarClearAction.text"),
                DevPilotMessageBundle.get("devpilot.toolbarClearAction.text"),
                DevPilotIcons.CLEAR_ICON);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DevPilotChatToolWindowFactory.getDevPilotChatToolWindow(e.getProject()).clearSession();
    }
}
