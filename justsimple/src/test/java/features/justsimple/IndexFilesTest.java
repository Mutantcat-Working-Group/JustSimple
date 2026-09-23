package features.justsimple;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.core.runtime.IndexFiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IndexFileUtil 测试类
 */
class IndexFilesTest {

    @Test
    void testGetIndexFileName() {
        String basePackage = "com.demo.app";
        String expected = "com-demo-app_bean_clz.index";
        
        assertEquals(expected, IndexFiles.getIndexFileName(basePackage,"bean_clz"));
    }

    @Test
    void testGetIndexFileNameWithSinglePackage() {
        String basePackage = "app";
        String expected = "app_bean_clz.index";
        
        assertEquals(expected, IndexFiles.getIndexFileName(basePackage,"bean_clz"));
    }
}