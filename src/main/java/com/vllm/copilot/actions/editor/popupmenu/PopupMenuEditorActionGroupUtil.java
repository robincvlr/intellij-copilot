package com.vllm.copilot.actions.editor.popupmenu;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.vllm.copilot.actions.notifications.DevPilotNotification;
import com.vllm.copilot.constant.DefaultConst;
import com.vllm.copilot.constant.PromptConst;
import com.vllm.copilot.enums.EditorActionEnum;
import com.vllm.copilot.enums.SessionTypeEnum;
import com.vllm.copilot.gui.toolwindows.DevPilotChatToolWindowFactory;
import com.vllm.copilot.gui.toolwindows.chat.DevPilotChatToolWindow;
import com.vllm.copilot.gui.toolwindows.components.EditorInfo;
import com.vllm.copilot.settings.actionconfiguration.EditorActionConfigurationState;
import com.vllm.copilot.settings.state.LanguageSettingsState;
import com.vllm.copilot.util.DevPilotMessageBundle;
import com.vllm.copilot.util.DocumentUtil;
import com.vllm.copilot.util.PerformanceCheckUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.Icon;

public class PopupMenuEditorActionGroupUtil {

    private static final Map<String, Icon> ICONS = new LinkedHashMap<>(Map.of(
            EditorActionEnum.PERFORMANCE_CHECK.getLabel(), AllIcons.Plugins.Updated,
            EditorActionEnum.GENERATE_COMMENTS.getLabel(), AllIcons.Actions.InlayRenameInCommentsActive,
            EditorActionEnum.GENERATE_TESTS.getLabel(), AllIcons.Modules.GeneratedTestRoot,
            EditorActionEnum.FIX_THIS.getLabel(), AllIcons.Actions.QuickfixBulb,
            EditorActionEnum.REVIEW_CODE.getLabel(), AllIcons.Actions.PreviewDetailsVertically,
            EditorActionEnum.EXPLAIN_THIS.getLabel(), AllIcons.Actions.Preview));

    public static void refreshActions(Project project) {
        AnAction actionGroup = ActionManager.getInstance().getAction("com.vllm.copilot.actions.editor.popupmenu.BasicEditorAction");
        if (actionGroup instanceof DefaultActionGroup) {
            DefaultActionGroup group = (DefaultActionGroup) actionGroup;
            group.removeAll();
            group.add(new NewChatAction());
            group.addSeparator();

            var defaultActions = EditorActionConfigurationState.getInstance().getDefaultActions();
            defaultActions.forEach((label, prompt) -> {
                var action = new BasicEditorAction(DevPilotMessageBundle.get(label), DevPilotMessageBundle.get(label), ICONS.getOrDefault(label, AllIcons.FileTypes.Unknown)) {
                    @Override
                    protected void actionPerformed(Project project, Editor editor, String selectedText) {
                        ToolWindow toolWindow = ToolWindowManager.getInstance(project).getToolWindow("DevPilot");
                        toolWindow.show();
                        if (isInputExceedLimit(selectedText, prompt)) {
                            DevPilotNotification.info(DevPilotMessageBundle.get("devpilot.notification.input.tooLong"));
                            return;
                        }

                        Consumer<String> callback = result -> {
                            if (validateResult(result)) {
                                DevPilotNotification.info(DevPilotMessageBundle.get("devpilot.notification.input.tooLong"));
                                return;
                            }

                            EditorActionEnum editorActionEnum = EditorActionEnum.getEnumByLabel(label);
                            if (Objects.isNull(editorActionEnum)) {
                                return;
                            }
                            switch (editorActionEnum) {
                                case PERFORMANCE_CHECK:
                                    // display result, and open diff window
                                    PerformanceCheckUtils.showDiffWindow(selectedText, project, editor);
                                    break;
                                case GENERATE_COMMENTS:
                                    DocumentUtil.diffCommentAndFormatWindow(project, editor, result);
                                    break;
                                default:
                                    break;
                            }
                        };

                        EditorInfo editorInfo = new EditorInfo(editor);

                        DevPilotChatToolWindow devPilotChatToolWindow = DevPilotChatToolWindowFactory.getDevPilotChatToolWindow(project);
                        // right action clear session
                        devPilotChatToolWindow.addClearSessionInfo();
                        String newPrompt = prompt.replace("{{selectedCode}}", selectedText);
                        devPilotChatToolWindow.syncSendAndDisplay(SessionTypeEnum.MULTI_TURN.getCode(), EditorActionEnum.getEnumByLabel(label), newPrompt,
                                callback, editorInfo);
                    }
                };
                group.add(action);
            });
        }
    }

    public static void registerOrReplaceAction(AnAction action) {
        ActionManager actionManager = ActionManager.getInstance();
        var actionId = action.getTemplateText();
        if (actionManager.getAction(actionId) != null) {
            actionManager.replaceAction(actionId, action);
        } else {
            actionManager.registerAction(actionId, action, PluginId.getId("com.vllm.copilot"));
        }
    }

    /**
     * check input length
     *
     * @return
     */
    private static boolean validateResult(String content) {
        return content.contains(DefaultConst.MAX_TOKEN_EXCEPTION_MSG);
    }

    /**
     * check length of input rather than max limit
     * 1 token = 3 english character()
     *
     * @param content
     * @return
     */
    private static boolean isInputExceedLimit(String content, String prompt) {
        // text too long, openai server always timeout
        if (content.length() + prompt.length() > DefaultConst.TOKEN_MAX_LENGTH) {
            return true;
        }
        return false;
    }

}
