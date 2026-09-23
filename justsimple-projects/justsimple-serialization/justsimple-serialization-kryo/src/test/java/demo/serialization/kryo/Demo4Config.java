package demo.serialization.kryo;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.kryo.KryoBytesSerializer;

/**
 * @author noear 2025/9/13 created
 */
@Configuration
public class Demo4Config {
    @Bean
    public void config(KryoBytesSerializer serializer) {
        serializer.bodyRequired();
    }
}