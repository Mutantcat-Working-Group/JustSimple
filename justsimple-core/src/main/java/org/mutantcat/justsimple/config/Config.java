package org.mutantcat.justsimple.config;

import io.netty.handler.codec.http.cors.CorsConfig;
import org.mutantcat.justsimple.annotation.Instance;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Instance
public class Config {
    Map<String, Object> config;
    CorsConfig corsConfig;

    public Config() {
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yaml");
            Map<String, Object> data = yaml.load(inputStream);
            this.config = data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(config.get("port")==null){
            config.put("port",7891);
        }
        corsConfig = null;
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
