package com.vllm.copilot.enums;

public enum ModelServiceEnum {
    LLAMA("LLaMA", "Code LLaMA (Locally)");

    // model name
    private final String name;

    // model display name
    private final String displayName;

    ModelServiceEnum(String name, String displayName) {
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public static ModelServiceEnum fromName(String name) {
        for (ModelServiceEnum type : ModelServiceEnum.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
