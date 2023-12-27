package com.vllm.copilot.integrations.llms.hub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intellij.openapi.components.Service;
import com.vllm.copilot.integrations.llms.entity.DevPilotChatCompletionRequest;
import com.vllm.copilot.integrations.llms.entity.DevPilotFailedResponse;
import com.vllm.copilot.integrations.llms.entity.DevPilotSuccessResponse;
import com.vllm.copilot.util.DevPilotMessageBundle;
import com.vllm.copilot.util.UserAgentUtils;
import com.vllm.copilot.integrations.llms.LlmProvider;
import com.vllm.copilot.settings.state.CodeLlamaSettingsState;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

@Service(Service.Level.PROJECT)
public final class HubServiceProvider implements LlmProvider {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private Call call;

    @Override
    public String chatCompletion(DevPilotChatCompletionRequest chatCompletionRequest) {
        var host = CodeLlamaSettingsState.getInstance().getModelHost();
        var model = CodeLlamaSettingsState.getInstance().getModel();

        if (StringUtils.isEmpty(host)) {
            return "Chat completion failed: host is empty";
        }

        if (StringUtils.isEmpty(model)) {
            return "Chat completion failed: model is empty";
        }

        okhttp3.Response response;

        try {
            var request = new Request.Builder()
                    .url(host + "/v1/chat/completions")
                    .header("User-Agent", UserAgentUtils.getUserAgent())
                    .post(RequestBody.create(objectMapper.writeValueAsString(chatCompletionRequest), MediaType.parse("application/json")))
                    .build();

            call = client.newCall(request);
            response = call.execute();
        } catch (Exception e) {
            return "Chat completion failed: " + e.getMessage();
        }

        try {
            return parseResult(chatCompletionRequest, response);
        } catch (IOException e) {
            return "Chat completion failed: " + e.getMessage();
        }
    }

    @Override
    public void interruptSend() {
        if (call != null && !call.isCanceled()) {
            call.cancel();
        }
    }

    private String parseResult(DevPilotChatCompletionRequest chatCompletionRequest, okhttp3.Response response) throws IOException {
        if (response == null) {
            return DevPilotMessageBundle.get("devpilot.chatWindow.response.null");
        }

        var result = Objects.requireNonNull(response.body()).string();

        if (response.isSuccessful()) {
            var message = objectMapper.readValue(result, DevPilotSuccessResponse.class)
                    .getChoices()
                    .get(0)
                    .getText();
            return message;

        } else {
            return objectMapper.readValue(result, DevPilotFailedResponse.class)
                    .getError()
                    .getMessage();
        }
    }
}
