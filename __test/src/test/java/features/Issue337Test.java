package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.test.JustSimpleTest;
import org.mutantcat.justsimple.JustSimple;
import webapp.App;

@JustSimpleTest(App.class)
public class Issue337Test {
    @Test
    public void testFolderLocation() {
        try {
            String appFolder = Utils.appFolder();
            System.out.println("文件路径: " + appFolder);
        } catch (Throwable e) {
            e.printStackTrace();  
        }
    }
}
