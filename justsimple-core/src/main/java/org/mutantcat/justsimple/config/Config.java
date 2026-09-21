package org.mutantcat.justsimple.config;

import io.netty.handler.codec.http.cors.CorsConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

//@Instance(name = "just_simple_config")
public class Config {
    private static final int DEFAULT_PORT = 7891;
    private static final String CONFIG_FILE = "application.yaml";

    private final Map<String, Object> config;
    private CorsConfig corsConfig;

    public Config() {
        Map<String, Object> loaded = new HashMap<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
        if (inputStream != null) {
            try (InputStream in = inputStream) {
                Object data = new Yaml().load(in);
                if (data instanceof Map) {
                    loaded.putAll((Map<String, Object>) data);
                }
            } catch (Exception e) {
                System.err.println("加载 " + CONFIG_FILE + " 失败,将使用默认配置: " + e.getMessage());
            }
        }
        if (loaded.get("port") == null) {
            loaded.put("port", DEFAULT_PORT);
        }
        this.config = loaded;
    }

    public Map<String, Object> getConfig() {
        return config;
    }

    public void setConfig(String name, Object config) {
        this.config.put(name, config);
    }

    public CorsConfig getCorsConfig() {
        return corsConfig;
    }

    public void setCorsConfig(CorsConfig corsConfig) {
        this.corsConfig = corsConfig;
    }
}