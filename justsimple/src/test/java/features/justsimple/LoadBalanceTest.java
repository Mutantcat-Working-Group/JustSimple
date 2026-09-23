package features.justsimple;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.core.LoadBalance;

import java.net.URI;

/**
 * @author noear 2024/10/12 created
 */
public class LoadBalanceTest {
    @Test
    public void case1() {
        LoadBalance tmp;
        tmp = LoadBalance.parse("https://localhost:8080");
        System.out.println(tmp.getServer());
        assert "https://localhost:8080".equals(tmp.getServer());

        tmp = LoadBalance.parse("https://localhost:8080/user/get");
        System.out.println(tmp.getServer());
        assert "https://localhost:8080".equals(tmp.getServer());
    }
}
