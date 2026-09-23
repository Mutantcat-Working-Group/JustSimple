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
package features;

import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;

/**
 * @author noear 2021/6/1 created
 */
@JustSimpleTest(App.class)
public class PropParseTest {
    @Test
    public void test(){
        assert  "justsimple.app.group".equals(JustSimple.cfg().getByTmpl("justsimple.app.group"));
        assert  "test".equals(JustSimple.cfg().getByTmpl("${justsimple.app.group}"));
        assert  "test_topic".equals(JustSimple.cfg().getByTmpl("${justsimple.app.group}_topic"));
        assert  "event_test_topic".equals(JustSimple.cfg().getByTmpl("event_${justsimple.app.group}_topic"));
        assert  "event_test".equals(JustSimple.cfg().getByTmpl("event_${justsimple.app.group}"));
    }
}
