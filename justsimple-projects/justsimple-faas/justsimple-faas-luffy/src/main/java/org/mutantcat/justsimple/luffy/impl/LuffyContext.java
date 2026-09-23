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
package org.mutantcat.justsimple.luffy.impl;

import org.mutantcat.justsimple.core.handle.Context;
import org.mutantcat.justsimple.core.handle.ContextAsyncListener;
import org.mutantcat.justsimple.core.handle.Cookie;
import org.mutantcat.justsimple.core.handle.DownloadedFile;
import org.mutantcat.justsimple.core.handle.SessionState;
import org.mutantcat.justsimple.core.handle.UploadedFile;
import org.mutantcat.justsimple.core.util.KeyValues;
import org.mutantcat.justsimple.core.util.MultiMap;
import org.mutantcat.justsimple.core.util.SupplierEx;
import org.noear.solon.core.handle.ContextEmpty;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Luffy 二进制接口绑定的是上游 Solon Context，这里把它适配为 JustSimple Context。
 *
 * @author noear
 * @since 1.3
 */
public class LuffyContext extends ContextEmpty {
    private final Context source;

    public LuffyContext(Context source) {
        this.source = source;
    }

    @Override
    public boolean isHeadersSent() {
        return source.isHeadersSent();
    }

    @Override
    public Object request() {
        return source.request();
    }

    @Override
    public String remoteIp() {
        return source.remoteIp();
    }

    @Override
    public int remotePort() {
        return source.remotePort();
    }

    @Override
    public int localPort() {
        return source.localPort();
    }

    @Override
    public String method() {
        return source.method();
    }

    @Override
    public String protocol() {
        return source.protocol();
    }

    @Override
    public URI uri() {
        return source.uri();
    }

    @Override
    public String path() {
        return source.path();
    }

    @Override
    public void pathNew(String pathNew) {
        source.pathNew(pathNew);
    }

    @Override
    public String pathNew() {
        return source.pathNew();
    }

    @Override
    public boolean isSecure() {
        return source.isSecure();
    }

    @Override
    public String url() {
        return source.url();
    }

    @Override
    public long contentLength() {
        return source.contentLength();
    }

    @Override
    public String contentType() {
        return source.contentType();
    }

    @Override
    public String contentCharset() {
        return source.contentCharset();
    }

    @Override
    public String queryString() {
        return source.queryString();
    }

    @Override
    public boolean autoMultipart() {
        return source.autoMultipart();
    }

    @Override
    public void autoMultipart(boolean auto) {
        source.autoMultipart(auto);
    }

    @Override
    public InputStream bodyAsStream() throws IOException {
        return source.bodyAsStream();
    }

    @Override
    public org.noear.solon.core.util.MultiMap<String> paramMap() {
        return adapt(source.paramMap());
    }

    @Override
    public org.noear.solon.core.util.MultiMap<org.noear.solon.core.handle.UploadedFile> fileMap() {
        return adapt(source.fileMap(), LuffyContext::adapt);
    }

    @Override
    public void filesDelete() throws IOException {
        source.filesDelete();
    }

    @Override
    public org.noear.solon.core.util.MultiMap<String> cookieMap() {
        return adapt(source.cookieMap());
    }

    @Override
    public org.noear.solon.core.util.MultiMap<String> headerMap() {
        return adapt(source.headerMap());
    }

    @Override
    public String sessionId() {
        return source.sessionId();
    }

    @Override
    public <T> T session(String name, Class<T> clz) {
        return source.session(name, clz);
    }

    @Override
    public <T> T sessionOrDefault(String name, T def) {
        return source.sessionOrDefault(name, def);
    }

    @Override
    public int sessionAsInt(String name) {
        return source.sessionAsInt(name);
    }

    @Override
    public int sessionAsInt(String name, int def) {
        return source.sessionAsInt(name, def);
    }

    @Override
    public long sessionAsLong(String name) {
        return source.sessionAsLong(name);
    }

    @Override
    public long sessionAsLong(String name, long def) {
        return source.sessionAsLong(name, def);
    }

    @Override
    public double sessionAsDouble(String name) {
        return source.sessionAsDouble(name);
    }

    @Override
    public double sessionAsDouble(String name, double def) {
        return source.sessionAsDouble(name, def);
    }

    @Override
    public void sessionSet(String name, Object val) {
        source.sessionSet(name, val);
    }

    @Override
    public void sessionRemove(String name) {
        source.sessionRemove(name);
    }

    @Override
    public void sessionClear() {
        source.sessionClear();
    }

    @Override
    public void sessionReset() {
        source.sessionReset();
    }

    @Override
    public org.noear.solon.core.handle.SessionState sessionState(boolean create) {
        SessionState state = source.sessionState(create);

        if (state == null) {
            return super.sessionState(create);
        } else {
            sessionState = new SessionStateAdapter(state);
            return (org.noear.solon.core.handle.SessionState) sessionState;
        }
    }

    @Override
    public org.noear.solon.core.handle.SessionState sessionState() {
        return sessionState(true);
    }

    @Override
    public Object response() {
        return source.response();
    }

    @Override
    protected void contentTypeDoSet(String contentType) {
        source.contentType(contentType);
    }

    @Override
    public void charset(String charset) {
        source.charset(charset);
        super.charset(charset);
    }

    @Override
    public void output(byte[] bytes) {
        source.output(bytes);
    }

    @Override
    public void output(InputStream stream) {
        source.output(stream);
    }

    @Override
    public void output(String str) {
        source.output(str);
    }

    @Override
    public void output(Throwable ex) {
        source.output(ex);
    }

    @Override
    public java.io.OutputStream outputStream() {
        try {
            return source.outputStream();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public java.util.zip.GZIPOutputStream outputStreamAsGzip() throws IOException {
        return source.outputStreamAsGzip();
    }

    @Override
    public void outputAsFile(org.noear.solon.core.handle.DownloadedFile file) throws IOException {
        source.outputAsFile(adapt(file));
    }

    @Override
    public void outputAsFile(File file) throws IOException {
        source.outputAsFile(file);
    }

    @Override
    public void headerSet(String name, String val) {
        source.headerSet(name, val);
    }

    @Override
    public void headerAdd(String name, String val) {
        source.headerAdd(name, val);
    }

    @Override
    public String headerOfResponse(String name) {
        return source.headerOfResponse(name);
    }

    @Override
    public Collection<String> headerValuesOfResponse(String name) {
        return source.headerValuesOfResponse(name);
    }

    @Override
    public Collection<String> headerNamesOfResponse() {
        return source.headerNamesOfResponse();
    }

    @Override
    public void cookieSet(org.noear.solon.core.handle.Cookie cookie) {
        source.cookieSet(adapt(cookie));
    }

    @Override
    public void redirect(String url, int code) {
        source.redirect(url, code);
    }

    @Override
    public void forward(String pathNew) {
        source.forward(pathNew);
    }

    @Override
    public int status() {
        return source.status();
    }

    @Override
    protected void statusDoSet(int status) {
        source.status(status);
    }

    @Override
    public void flush() throws IOException {
        source.flush();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public boolean asyncSupported() {
        return source.asyncSupported();
    }

    @Override
    public void asyncListener(org.noear.solon.core.handle.ContextAsyncListener listener) {
        source.asyncListener(new AsyncListenerAdapter(listener));
    }

    @Override
    public void asyncStart(long timeout, Runnable runnable) {
        source.asyncStart(timeout, runnable);
    }

    @Override
    public boolean asyncStarted() {
        return source.asyncStarted();
    }

    @Override
    public void asyncComplete() {
        source.asyncComplete();
    }

    @Override
    public Map<String, Object> attrMap() {
        return source.attrMap();
    }

    @Override
    public <T> T attr(String name) {
        return source.attr(name);
    }

    @Override
    public <T> T attrOrDefault(String name, T def) {
        return source.attrOrDefault(name, def);
    }

    @Override
    public void attrSet(String name, Object val) {
        source.attrSet(name, val);
    }

    @Override
    public void attrSet(Map<String, Object> map) {
        source.attrSet(map);
    }

    @Override
    public Collection<String> attrNames() {
        return source.attrNames();
    }

    @Override
    public void attrsClear() {
        source.attrsClear();
    }

    @Override
    public boolean remoting() {
        return source.remoting();
    }

    @Override
    public void remotingSet(boolean remoting) {
        source.remotingSet(remoting);
    }

    @Override
    public void returnValue(Object obj) throws Throwable {
        source.returnValue(obj);
    }

    private static org.noear.solon.core.util.MultiMap<String> adapt(MultiMap<String> from) {
        return adapt(from, value -> value);
    }

    private static <T, R> org.noear.solon.core.util.MultiMap<R> adapt(MultiMap<T> from, Function<? super T, ? extends R> mapper) {
        org.noear.solon.core.util.MultiMap<R> to = new org.noear.solon.core.util.MultiMap<R>();

        if (from != null) {
            for (KeyValues<T> kv : from) {
                List<T> values = kv.getValues();

                if (values != null) {
                    for (T value : values) {
                        to.add(kv.getKey(), mapper.apply(value));
                    }
                }
            }
        }

        return to;
    }

    private static org.noear.solon.core.handle.UploadedFile adapt(UploadedFile file) {
        if (file == null) {
            return null;
        } else {
            return new org.noear.solon.core.handle.UploadedFile(
                    file.getContentType(),
                    file.getContentSize(),
                    file.getContent(),
                    file.getName(),
                    file.getExtension());
        }
    }

    private static Cookie adapt(org.noear.solon.core.handle.Cookie cookie) {
        if (cookie == null) {
            return null;
        } else {
            Cookie out = new Cookie(cookie.name, cookie.value);
            out.domain = cookie.domain;
            out.path = cookie.path;
            out.maxAge = cookie.maxAge;
            out.secure = cookie.secure;
            out.httpOnly = cookie.httpOnly;
            return out;
        }
    }

    private static DownloadedFile adapt(org.noear.solon.core.handle.DownloadedFile file) throws IOException {
        if (file == null) {
            return null;
        } else {
            SupplierEx<InputStream> content = file::getContent;
            return new DownloadedFile(file.getContentType(), file.getContentSize(), content, file.getName());
        }
    }

    private static class SessionStateAdapter implements org.noear.solon.core.handle.SessionState {
        private final SessionState source;

        private SessionStateAdapter(SessionState source) {
            this.source = source;
        }

        @Override
        public boolean replaceable() {
            return source.replaceable();
        }

        @Override
        public long creationTime() {
            return source.creationTime();
        }

        @Override
        public long lastAccessTime() {
            return source.lastAccessTime();
        }

        @Override
        public void sessionRefresh() throws IOException {
            source.sessionRefresh();
        }

        @Override
        public void sessionPublish() throws IOException {
            source.sessionPublish();
        }

        @Override
        public void sessionClear() {
            source.sessionClear();
        }

        @Override
        public void sessionReset() {
            source.sessionReset();
        }

        @Override
        public String sessionId() {
            return source.sessionId();
        }

        @Override
        public String sessionChangeId() {
            return source.sessionChangeId();
        }

        @Override
        public Collection<String> sessionKeys() {
            return source.sessionKeys();
        }

        @Override
        public Object sessionGet(String key) {
            return source.sessionGet(key);
        }

        @Override
        public <T> T sessionGet(String key, Class<T> clz) {
            return source.sessionGet(key, clz);
        }

        @Override
        public void sessionSet(String key, Object val) {
            source.sessionSet(key, val);
        }

        @Override
        public void sessionRemove(String key) {
            source.sessionRemove(key);
        }

        @Override
        public String sessionToken() {
            return source.sessionToken();
        }
    }

    private static class AsyncListenerAdapter implements ContextAsyncListener {
        private final org.noear.solon.core.handle.ContextAsyncListener source;

        private AsyncListenerAdapter(org.noear.solon.core.handle.ContextAsyncListener source) {
            this.source = source;
        }

        @Override
        public void onStart(Context ctx) throws IOException {
            source.onStart(new LuffyContext(ctx));
        }

        @Override
        public void onComplete(Context ctx) throws IOException {
            source.onComplete(new LuffyContext(ctx));
        }

        @Override
        public void onTimeout(Context ctx) throws IOException {
            source.onTimeout(new LuffyContext(ctx));
        }

        @Override
        public void onError(Context ctx, Throwable e) throws IOException {
            source.onError(new LuffyContext(ctx), e);
        }
    }
}
