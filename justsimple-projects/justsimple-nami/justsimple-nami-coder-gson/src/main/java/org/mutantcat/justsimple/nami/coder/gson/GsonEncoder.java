package org.mutantcat.justsimple.nami.coder.gson;

import org.mutantcat.justsimple.nami.Context;
import org.mutantcat.justsimple.nami.Encoder;
import org.mutantcat.justsimple.nami.common.ContentTypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 编码器:负责把对象编码为json
 * 
 * @author cqyhm
 * @since 2025年12月30日13:25:26
 */
public class GsonEncoder implements Encoder {
    public static final GsonEncoder instance = new GsonEncoder();

    private Gson gson;
    public GsonEncoder() {
    	this.gson = new GsonBuilder()
    			//.registerTypeAdapter(Date.class, new TimestampSecondsAdapter())
    			.create();
    }
    @Override
    public String enctype() {
        return ContentTypes.JSON_VALUE;
    }

    @Override
    public byte[] encode(Object obj) {
        return gson.toJson(obj).getBytes();
    }

    @Override
    public void pretreatment(Context ctx) {

    }
}
