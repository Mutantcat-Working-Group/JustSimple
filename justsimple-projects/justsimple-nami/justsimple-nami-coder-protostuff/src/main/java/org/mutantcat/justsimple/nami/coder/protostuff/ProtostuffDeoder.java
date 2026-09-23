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
package org.mutantcat.justsimple.nami.coder.protostuff;

import org.mutantcat.justsimple.nami.Decoder;
import org.mutantcat.justsimple.nami.Context;
import org.mutantcat.justsimple.nami.Result;
import org.mutantcat.justsimple.nami.common.ContentTypes;
import org.mutantcat.justsimple.serialization.protostuff.ProtostuffBytesSerializer;

import java.lang.reflect.Type;

/**
 * @author noear
 * @since 1.2
 */
public class ProtostuffDeoder implements Decoder {
    public static final ProtostuffDeoder instance = new ProtostuffDeoder();

    private final ProtostuffBytesSerializer serializer = ProtostuffBytesSerializer.getDefault();


    @Override
    public String enctype() {
        return ContentTypes.PROTOBUF_VALUE;
    }

    @Override
    public <T> T decode(Result rst, Type type) throws Exception {
        if (rst.body().length == 0) {
            return null;
        }

        return (T) serializer.deserialize(rst.body(), type);
    }

    @Override
    public void pretreatment(Context ctx) {
        ctx.headers.put(ContentTypes.HEADER_SERIALIZATION, ContentTypes.AT_PROTOBUF);
        ctx.headers.put(ContentTypes.HEADER_ACCEPT, ContentTypes.PROTOBUF_VALUE);
    }
}
