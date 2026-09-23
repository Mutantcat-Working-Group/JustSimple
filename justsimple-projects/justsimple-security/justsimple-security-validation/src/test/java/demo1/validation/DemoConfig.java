package demo1.validation;

import org.mutantcat.justsimple.annotation.Bean;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.validation.ValidatorFailureHandler;
import org.mutantcat.justsimple.validation.ValidatorFailureHandlerI18n;

@Configuration
public class DemoConfig {
    /**
     * 支持：message="{aaa} bb {ccc.ddd}"
     * */
    @Bean
    public ValidatorFailureHandler validatorFailureHandler() {
        return new ValidatorFailureHandlerI18n(2048);
    }
}
