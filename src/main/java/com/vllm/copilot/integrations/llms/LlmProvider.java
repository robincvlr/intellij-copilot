package com.vllm.copilot.integrations.llms;

import com.vllm.copilot.integrations.llms.entity.DevPilotChatCompletionRequest;

public interface LlmProvider {

    String chatCompletion(DevPilotChatCompletionRequest chatCompletionRequest);

    void interruptSend();

}
