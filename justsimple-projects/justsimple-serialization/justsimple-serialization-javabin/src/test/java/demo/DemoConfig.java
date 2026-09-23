package demo;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.serialization.javabin.JavabinSerializer;

/**
 *
 * @author noear 2026/5/18 created
 *
 */
@Configuration
public class DemoConfig {
    @Bean
    public void demo(JavabinSerializer serializer) {
        //模式示例
        serializer.classFilter().allow("demo.");

        //全允许示例
        serializer.classFilter().allowAll(true);
    }
}
