package org.mutantcat.justsimple.request;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.mutantcat.justsimple.web.FileUploadHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Context {
    FullHttpRequest fullHttpRequest;
    ChannelHandlerContext channelHandlerContext;
    Map<String, List<String>> getParams;
    String json;
    Map<String, Object> formData;

    public Context(FullHttpRequest fullHttpRequest) {
        this.fullHttpRequest = fullHttpRequest;
    }

    public Context(FullHttpRequest fullHttpRequest, Map<String, List<String>> getParams) {
        this.fullHttpRequest = fullHttpRequest;
        this.getParams = getParams;
    }

    public Context(FullHttpRequest fullHttpRequest, Map<String, List<String>> getParams,String json) {
        this.fullHttpRequest = fullHttpRequest;
        this.getParams = getParams;
        this.json = json;
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
        List<String> params = getParams.get(name);
        if (params == null) {
            return new ArrayList<>();
        } else {
            return params;
        }
    }

    public String getJson() {
        return json;
    }

    public Map<String, Object> getFormData() {
        return formData;
    }

    public Object getFormDataByKey(String key) {
        return formData.get(key);
    }

    public FileUploadHandler.TempFile getFileUploadByKey(String key) {
        return (FileUploadHandler.TempFile) formData.get(key);
    }

}
