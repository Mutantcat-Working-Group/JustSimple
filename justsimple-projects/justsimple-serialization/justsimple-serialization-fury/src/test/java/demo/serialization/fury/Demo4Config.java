package demo.serialization.fury;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.fury.FuryBytesSerializer;

/**
 * @author noear 2025/9/13 created
 */
@Configuration
public class Demo4Config {
    @Bean
    public void config(FuryBytesSerializer serializer) throws Exception {
        serializer.bodyRequired();
    }
}