package demo;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.data.annotation.Ds;

import javax.sql.DataSource;


/**
 * @author noear 2025/8/27 created
 */
@Configuration
public class DsConfig {
    @Bean
    public void dsCfg(@Ds("ds1") DataSource ds) {
        //ds.setUserName(xxx); //修改
    }
}
