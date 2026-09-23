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
package webapp.demo5_rpc;

import org.mutantcat.justsimple.nami.Nami;
import org.mutantcat.justsimple.nami.NamiAttach;
import org.mutantcat.justsimple.nami.coder.snack4.Snack4Decoder;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.Handler;

import java.util.HashMap;
import java.util.Map;

@Mapping("/demo5/rpctest/")
@Controller
public class RpcTest implements Handler {
    @Override
    public void handle(Context ctx) throws Throwable {
        Map<String, Object> map = new HashMap<>();

        NamiAttach.apply(attach -> {
            attach.put("user_name", "noear");

            map.put("HttpChannel", httpOf());
            map.put("SocketChannel", socketOf());
        });

        ctx.render(map);
    }

    protected void testOf() {
        NamiAttach.apply(attach -> {
            attach.put("user_name", "noear");
        });
    }

    private Object httpOf() {
        String root = "http://localhost:" + JustSimple.cfg().serverPort();

        RockApi client = Nami.builder()
                .decoder(Snack4Decoder.instance)
                .upstream(() -> root)
                .create(RockApi.class);

        return client.test1(12);
    }

    private Object socketOf() {
        int _port = 20000 + JustSimple.cfg().serverPort();

        RockApi client = Nami.builder().upstream(() -> "tcp://localhost:" + _port)
                .decoder(Snack4Decoder.instance)
                .create(RockApi.class);

        return client.test1(12);
    }
}