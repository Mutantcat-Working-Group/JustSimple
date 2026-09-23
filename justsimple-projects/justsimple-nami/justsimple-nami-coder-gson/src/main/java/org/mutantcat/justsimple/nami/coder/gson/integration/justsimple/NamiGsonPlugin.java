package org.mutantcat.justsimple.nami.coder.gson.integration.justsimple;

import org.mutantcat.justsimple.nami.NamiManager;
import org.mutantcat.justsimple.nami.coder.gson.GsonDecoder;
import org.mutantcat.justsimple.nami.coder.gson.GsonEncoder;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.Plugin;

/**
 * gson插件,负责注册编解码器
 * 
 * @author cqyhm
 * @since 2025年12月30日13:24:30
 */
public class NamiGsonPlugin implements Plugin {
    @Override
    public void start(AppContext context) {
        NamiManager.reg(GsonDecoder.instance);
        NamiManager.reg(GsonEncoder.instance);
    }
}
