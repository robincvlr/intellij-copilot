package com.vllm.copilot.constant;

import com.vllm.copilot.util.DevPilotMessageBundle;

public class DefaultConst {

    public final static String MAX_TOKEN_EXCEPTION_MSG = DevPilotMessageBundle.get("devpilot.chatWindow.context.overflow");

    public final static int TOKEN_MAX_LENGTH = 4096;

    public final static int ENGLISH_CONTENT_MAX_LENGTH = 12288;

    public final static String DEFAULT_CODE_LANGUAGE = "java";

}