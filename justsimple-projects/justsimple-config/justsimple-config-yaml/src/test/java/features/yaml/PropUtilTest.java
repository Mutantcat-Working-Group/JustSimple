package features.yaml;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.Utils;

import java.util.Properties;

/**
 * @author noear 2025/5/29 created
 */
public class PropUtilTest {
    /**
     * Properties.toString() 的输出顺序由 Hashtable 的哈希桶决定，
     * 与插入顺序无关，所以这里只比较键值对本身，不比较输出顺序。
     */
    private static boolean propEquals(Properties properties, String... kv) {
        if (properties.size() != kv.length / 2) {
            return false;
        }

        for (int i = 0; i < kv.length; i += 2) {
            if (kv[i + 1].equals(properties.getProperty(kv[i])) == false) {
                return false;
            }
        }

        return true;
    }

    @Test
    public void jsonCase1() {
        Properties properties = Utils.buildProperties("{\"a\":\"a\",\"b\":\"b\"}");
        System.out.println(properties);
        assert propEquals(properties, "a", "a", "b", "b");
    }

    @Test
    public void jsonCase2() {
        Properties properties = Utils.buildProperties("[1,2]");
        System.out.println(properties);
        assert propEquals(properties, "[0]", "1", "[1]", "2");
    }

    @Test
    public void yamlCase1() {
        Properties properties = Utils.buildProperties("a: a\n" +
                "b: b");
        System.out.println(properties);
        assert propEquals(properties, "a", "a", "b", "b");
    }

    @Test
    public void yamlCase2() {
        Properties properties = Utils.buildProperties("- 1\n" +
                "- 2");
        System.out.println(properties);
        assert propEquals(properties, "[0]", "1", "[1]", "2");
    }

    @Test
    public void propCase1() {
        Properties properties = Utils.buildProperties("a=a\n" +
                "b=b");
        System.out.println(properties);
        assert propEquals(properties, "a", "a", "b", "b");
    }

    @Test
    public void propCase2() {
        Properties properties = Utils.buildProperties("[0]=1\n" +
                "[1]=2");
        System.out.println(properties);
        assert propEquals(properties, "[0]", "1", "[1]", "2");
    }
}
