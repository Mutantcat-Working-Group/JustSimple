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
package org.mutantcat.justsimple.serialization;

import org.mutantcat.justsimple.core.handle.AbstractEntityConverter;
import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.util.Assert;

import java.util.Base64;

/**
 * 虚拟字节码实体转换器
 *
 * @author noear
 * @since 3.6
 */
public abstract class AbstractBytesEntityConverter<T extends EntitySerializer<byte[]>> extends AbstractEntityConverter {
    protected final T serializer;

    public T getSerializer() {
        return serializer;
    }

    public AbstractBytesEntityConverter(T serializer) {
        Assert.notNull(serializer, "Serializer not be null");
        this.serializer = serializer;
    }

    @Override
    public boolean allowWrite() {
        return true;
    }

    @Override
    public boolean canWrite(String mime, Context ctx) {
        return serializer.matched(ctx, mime);
    }

    @Override
    public String writeAndReturn(Object data, Context ctx) throws Throwable {
        byte[] tmp = serializer.serialize(data);
        return Base64.getEncoder().encodeToString(tmp);
    }

    @Override
    public void write(Object data, Context ctx) throws Throwable {
        if (SerializationConfig.isOutputMeta()) {
            ctx.headerAdd("justsimple.serialization", name());
        }

        serializer.serializeToBody(ctx, data);
    }

    @Override
    public boolean allowRead() {
        return true;
    }

    @Override
    public boolean canRead(Context ctx, String mime) {
        return serializer.matched(ctx, mime);
    }
}