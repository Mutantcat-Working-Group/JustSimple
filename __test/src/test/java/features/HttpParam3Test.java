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
import org.mutantcat.justsimple.nami.Nami;
import org.mutantcat.justsimple.nami.annotation.NamiBody;
import org.mutantcat.justsimple.nami.annotation.NamiClient;
import org.mutantcat.justsimple.nami.coder.snack4.Snack4Decoder;
import org.mutantcat.justsimple.test.JustSimpleTest;
import webapp.App;
import webapp.models.UserModel;

/**
 * @author noear 2021/1/5 created
 */
@JustSimpleTest(App.class)
public class HttpParam3Test {
    @Test
    public void nami() {
        ParamService paramService = Nami.builder()
                .decoder(Snack4Decoder.instance)
                .create(ParamService.class);

        UserModel userModel = new UserModel();
        userModel.setName("noear");

        UserModel model2 = paramService.model(userModel);

        assert userModel.getName().equals(model2.getName());
    }


    @NamiClient(path = "/demo2/param", upstream = {"http://localhost:8080"})
    public interface ParamService {
        UserModel model(@NamiBody UserModel model);
    }
}
