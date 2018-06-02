package com.haotian.analysis.ui.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class ConfigUtil {
    private static final Config INSTANCE;
    static {
        InputStream inputStream = null;
        inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream("config.json");
        ObjectMapper om = new ObjectMapper();
        Config config;
        try {
            config = om.readValue(inputStream, Config.class);
        } catch (IOException e) {
            throw new IllegalStateException("init default config error", e);
        }
        INSTANCE = config;
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }
        try {
            inputStream.close();
        } catch (IOException e) {
            throw new IllegalStateException("close stream error", e);
        }
    }

    public static Config getDefaultConfig() {
        return INSTANCE;
    }
}
