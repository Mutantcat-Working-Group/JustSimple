package org.mutantcat.justsimple.config;

import org.mutantcat.justsimple.annotation.Instance;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

@Instance
public class Config {
    Map<String, Object> config;

    public Config(){
        Yaml yaml = new Yaml();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("application.yaml");
            Map<String, Object> data = yaml.load(inputStream);
            this.config = data;
            // System.out.println(data);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<String, Object> getConfig() {
        return config;
    }
}
