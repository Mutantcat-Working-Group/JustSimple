/*
 * Copyright 2017-2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package labs.serialization.jackson_xml;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;


/**
 * @author noear 2021/10/12 created
 */
@JustSimpleTest(TestApp.class)
public class TestDemo extends HttpTester {
    @Test
    public void test0() throws Exception{
        //xml 序列化器只按 Accept / X-Serialization 匹配，需显式声明
        //（本模块没有注册 @json 渲染器，不带 Accept 时会回退成对象 toString）
        String xml = path("/").header("Accept", "text/xml").get();

        //时间类型走定制编码器：LocalDateTime->yyyy-MM-dd HH:mm，LocalDate->yyyy-MM-dd，Date->毫秒
        assert  tagValue(xml, "time1").length() == 16;
        assert  tagValue(xml, "time2").length() == 10;
        assert  Long.parseLong(tagValue(xml, "time3")) > 1000000000L;
    }

    @Test
    public void hello_test() throws Exception {
        String json = path("/hello").bodyOfJson("").post();
        //xml body 不参与 java.lang.* 参数的绑定（见 JacksonXmlEntityConverter#changeValue）
        assert "".equals(json);

        json = path("/hello?name=world").bodyOfJson("").post();
        assert "world".equals(json);

        //body 与 query 同时存在时，query 优先
        json = path("/hello?name=world").body("<name>world</name>", "text/xml").post();
        assert "world".equals(json);
    }

    /**
     * 取 xml 里同名标签的文本值
     */
    private static String tagValue(String xml, String tag) {
        String open = "<" + tag + ">";
        int s = xml.indexOf(open);

        if (s < 0) {
            return "";
        }

        int e = xml.indexOf("</" + tag + ">", s);

        return xml.substring(s + open.length(), e);
    }
}
