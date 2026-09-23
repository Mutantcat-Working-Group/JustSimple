package demo.serialization.abc;

import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.abc.AbcBytesSerializer;

/**
 *
 * @author noear 2025/9/13 created
 *
 */
@Configuration
public class Demo4Config {
    public void config(AbcBytesSerializer serializer) throws Exception {
        serializer.bodyRequired();
    }
}