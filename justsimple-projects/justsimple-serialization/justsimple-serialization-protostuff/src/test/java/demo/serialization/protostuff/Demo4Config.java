package demo.serialization.protostuff;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.protostuff.ProtostuffBytesSerializer;

/**
 * @author noear 2025/9/13 created
 */
@Configuration
public class Demo4Config {
    @Bean
    public void config(ProtostuffBytesSerializer serializer) {
        serializer.bodyRequired();
    }
}
