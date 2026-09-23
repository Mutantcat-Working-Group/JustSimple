package demo.serialization.fory;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.fory.ForyBytesSerializer;

/**
 * @author noear 2025/9/13 created
 */
@Configuration
public class Demo4Config {
    @Bean
    public void config(ForyBytesSerializer serializer) throws Exception {
        serializer.bodyRequired();
    }
}