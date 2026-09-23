package demo.httputils;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.net.http.HttpExtension;
import org.mutantcat.justsimple.net.http.HttpSslSupplier;
import org.mutantcat.justsimple.net.http.HttpUtils;
import org.mutantcat.justsimple.net.http.impl.HttpSslSupplierDefault;

import javax.net.ssl.*;

/**
 *
 * @author noear 2025/8/8 created
 *
 */
@Managed
public class HttpSslExtensionDemo extends HttpSslSupplierDefault implements HttpExtension, HttpSslSupplier {
    // for HttpExtension
    @Override
    public void onInit(HttpUtils httpUtils, String url) {
        httpUtils.ssl(this);
    }

    @Override
    public SSLContext getSslContext() {
        return getAnySslContext();
    }
}
