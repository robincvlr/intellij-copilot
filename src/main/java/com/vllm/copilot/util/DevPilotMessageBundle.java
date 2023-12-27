package com.vllm.copilot.util;

import com.intellij.DynamicBundle;

import java.util.Locale;
import java.util.ResourceBundle;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

public class DevPilotMessageBundle extends DynamicBundle {

    private static final DevPilotMessageBundle INSTANCE = new DevPilotMessageBundle();

    private static ResourceBundle bundleEn;

    private static final String pathToBundle = "messages.devpilot";

    private DevPilotMessageBundle() {
        super(pathToBundle);
    }

    static {
        bundleEn = ResourceBundle.getBundle(pathToBundle, Locale.ENGLISH);
    }

    public static String get(@NotNull @PropertyKey(resourceBundle = "messages.devpilot") String key) {
        return bundleEn.getString(key);
    }

    public static String get(@NotNull @PropertyKey(resourceBundle = "messages.devpilot") String key, Object... params) {
        return INSTANCE.getMessage(key, params);
    }

}