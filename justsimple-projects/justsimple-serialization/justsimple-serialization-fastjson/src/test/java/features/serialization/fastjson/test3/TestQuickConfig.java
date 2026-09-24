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
package features.serialization.fastjson.test3;

import features.serialization.fastjson.model.UserDo;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.annotation.Import;
import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.core.handle.ContextEmpty;
import org.mutantcat.justsimple.serialization.fastjson.FastjsonEntityConverter;
import org.mutantcat.justsimple.test.JustSimpleTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 时间进行格式化 + long,int 转为字符串 + 常见类型转为非null
 */
@Import(profiles = "classpath:features2_test3.yml")
@JustSimpleTest
public class TestQuickConfig {
    @Inject
    FastjsonEntityConverter entityConverter;

    @Test
    public void hello2() throws Throwable{
        UserDo userDo = new UserDo();

        Map<String, Object> data = new HashMap<>();
        data.put("time", new Date(1673861993477L));
        data.put("long", 12L);
        data.put("int", 12);
        data.put("null", null);

        userDo.setMap1(data);

        ContextEmpty ctx = new ContextEmpty();
        entityConverter.write(userDo, ctx);
        String output = ctx.attr("output");

        System.out.println(output);

        //nullStringAsEmpty/nullBoolAsFalse/nullNumberAsZero/nullArrayAsEmpty 生效，
        //但 nullAsWriteable 为 false，故 map0/obj0 的 null 字段被省略；
        //布尔经 boolAsInt 输出 0/1；long 型 null 由 nullNumberAsZero 写成数字 0（不走 longAsString）
        assert "{\"b0\":0,\"b1\":1,\"d0\":0,\"d1\":1.0,\"list0\":[],\"map1\":{\"time\":\"2023-01-16 17:39:53\",\"long\":\"12\",\"int\":12},\"n0\":0,\"n1\":\"1\",\"s0\":\"\",\"s1\":\"noear\",\"type\":\"MANAGE\"}".equals(output);
    }
}
