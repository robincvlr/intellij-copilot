package com.vllm.copilot.integrations.llms.entity;

import java.util.ArrayList;
import java.util.List;

public class DevPilotChatCompletionRequest {

    String model;

    Integer temperature;

    Integer maxTokens;

    List<DevPilotMessage> messages = new ArrayList<>();

    boolean stream = Boolean.FALSE;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<DevPilotMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<DevPilotMessage> messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public Integer getTemperature() {
        return temperature;
    }

    public void setTemperature(Integer temperature) {
        this.temperature = temperature;
    }

    public Integer getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(Integer maxTokens) {
        this.maxTokens = maxTokens;
    }


}
