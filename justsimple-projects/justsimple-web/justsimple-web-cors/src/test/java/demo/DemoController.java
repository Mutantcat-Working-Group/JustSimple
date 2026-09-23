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
package demo;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Get;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.annotation.Post;

/**
 * @author noear 2022/4/28 created
 */
@Controller
public class DemoController extends BaseController{
    @Get
    @Post
    @Mapping("hello")
    public String hello(){
        return "hello";
    }

    @Mapping("hello2")
    public String hello2(){
        return "hello";
    }
}
