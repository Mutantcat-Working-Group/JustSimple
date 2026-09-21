package org.mutantcat.justsimple.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.mutantcat.justsimple.web.FileUploadHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Context {
    private final FullHttpRequest fullHttpRequest;
    private ChannelHandlerContext channelHandlerContext;
    private final Map<String, List<String>> getParams;
    private final String json;
    private final Map<String, Object> formData;

    public Context(FullHttpRequest fullHttpRequest) {
        this(fullHttpRequest, null, null, null);
    }

    public Context(FullHttpRequest fullHttpRequest, Map<String, List<String>> getParams) {
        this(fullHttpRequest, getParams, null, null);
    }

    public Context(FullHttpRequest fullHttpRequest, Map<String, List<String>> getParams, String json) {
        this(fullHttpRequest, getParams, json, null);
    }

    public Context(FullHttpRequest request, Map<String, List<String>> parameters, String json, Map<String, Object> formData) {
        this.fullHttpRequest = request;
        this.getParams = parameters;
        this.json = json;
        this.formData = formData;
    }

    public FullHttpRequest getFullHttpRequest() {
        return fullHttpRequest;
    }

    public ChannelHandlerContext getChannelHandlerContext() {
        return channelHandlerContext;
    }

    public List<String> getParam(String name) {
        List<String> params = getParams == null ? null : getParams.get(name);
        if (params == null) {
            return Collections.emptyList();
        }
        return params;
    }

    public String getJson() {
        return json;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public Object getFormDataByKey(String key) {
        return formData == null ? null : formData.get(key);
    }

    public FileUploadHandler.TempFile getFileUploadByKey(String key) {
        if (formData == null) {
            return null;
        }
        Object value = formData.get(key);
        return value instanceof FileUploadHandler.TempFile ? (FileUploadHandler.TempFile) value : null;
    }
}