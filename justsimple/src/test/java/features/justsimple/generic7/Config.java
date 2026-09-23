package features.justsimple.generic7;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.annotation.Inject;

/**
 * @author noear 2025/6/3 created
 */
@Configuration
public class Config {
    @Managed(name = "loginKit", typed = true)
    public LoginKit<LoginUser> loginKit(@Inject ILoginUserFactory<LoginUser> loginUserFactory) {
        return new LoginKit<>(loginUserFactory);
    }

    @Managed
    public ILoginUserFactory<LoginUser> loginUserFactory() {
        return new ILoginUserFactory<LoginUser>() {
            @Override
            public LoginUser create() {
                return null;
            }
        };
    }
}
