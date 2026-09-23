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
package webapp;

//import cn.dev33.satoken.SaManager;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.JustSimpleApp;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.annotation.*;
import org.noear.solon.cloud.CloudClient;
import org.mutantcat.justsimple.core.AppContext;
import org.mutantcat.justsimple.core.ExtendLoader;
import org.mutantcat.justsimple.core.event.AppInitEndEvent;
import org.mutantcat.justsimple.core.util.RunUtil;
import org.mutantcat.justsimple.scheduling.annotation.EnableAsync;
import org.mutantcat.justsimple.scheduling.annotation.EnableRetry;
import org.mutantcat.justsimple.security.web.SecurityFilter;
import org.mutantcat.justsimple.security.web.header.XContentTypeOptionsHeaderHandler;
import org.mutantcat.justsimple.serialization.EntityStringSerializer;
import org.mutantcat.justsimple.serialization.properties.PropertiesStringSerializer;
import org.mutantcat.justsimple.server.http.HttpServerConfigure;
import org.mutantcat.justsimple.view.freemarker.FreemarkerRender;
import org.mutantcat.justsimple.web.staticfiles.StaticMappings;
import org.mutantcat.justsimple.web.staticfiles.repository.ClassPathStaticRepository;
import org.mutantcat.justsimple.web.staticfiles.repository.ExtendStaticRepository;
import org.mutantcat.justsimple.web.staticfiles.repository.FileStaticRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webapp.demo2_mvc.PathPrefixController;
import webapp.demo6_aop.TestImport;

@Managed
@EnableAsync
@EnableRetry
//@EnableScheduling
@Import(classes = TestImport.class, scanPackages = "webapp")
@JustSimpleMain
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    @Inject
    AppContext appContext;

    public static void main(String[] args) throws Exception {
        if (JustSimple.app() != null) {
            return;
        }

        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("user.dir=" + System.getProperty("user.dir"));
        System.out.println("app.dir=" + Utils.appFolder());
        System.out.println("resource[/]=" + App.class.getResource("/").getPath());
        System.out.println("resource[]=" + App.class.getResource("").getPath());

        System.getenv().forEach((k, v) -> {
            System.out.println("ENV: " + k + "=" + v);
        });

        //简化方式
        //JustSimpleApp app = JustSimple.start(TestApp.class, args, x -> x.enableSocketD(true).enableWebSocket(true));


        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

        //构建方式

        JustSimple.start(App.class, args, x -> {
            x.enableSocketD(true);
            x.enableWebSocket(true);

            //x.converterManager().register(new CatTypeConverter());

            //x.onStatus(404, c->c.render("404了"));

            x.router().filter(new SecurityFilter(new XContentTypeOptionsHeaderHandler()));

            x.factories().threadLocalFactory((applyFor, inheritance0) -> {
                if (inheritance0) {
                    return new InheritableThreadLocal();
                } else {
                    return new ThreadLocal();
                }
            });

            x.context().getBeanAsync(EntityStringSerializer.class, e -> {
                System.out.println("EntityStringSerializer event: xxxxx: " + e.getClass().getSimpleName());
            });

            x.context().getBeanAsync(PropertiesStringSerializer.class, e -> {
                e.allowPostForm(true);
            });

            x.context().getBeanAsync(HttpServerConfigure.class, e -> {
                //e.enableDebug(true);
            });

            x.context().getBeanAsync(FreemarkerRender.class, e -> {
                System.out.println("%%%%%%%%%%%%%%%%%%");
                RunUtil.runOrThrow(() -> e.getProvider().setSetting("classic_compatible", "true"));
            });

            x.onEvent(AppInitEndEvent.class, e -> {
                StaticMappings.add("/", new ExtendStaticRepository());
            });

            x.router().addPathPrefix("/pp1/", clz -> clz == PathPrefixController.class);

            StaticMappings.add("/file-a/", new ClassPathStaticRepository("static_test2"));
            StaticMappings.add("/ext/", new ExtendStaticRepository());
            StaticMappings.add("/sa-token/", new FileStaticRepository("/Users/noear/Downloads/"));
            StaticMappings.add("/down/JustSimple-0.1.1.zip", new FileStaticRepository("/Users/noear/Movies/"));
            StaticMappings.add("/down/PdfLoaderTest.pdf", new FileStaticRepository("/Users/noear/Movies/"));
            StaticMappings.add("/down/Socket.D-JS uniapp demo.mov", new FileStaticRepository("/Users/noear/Movies/"));

            // 测试用例开启调试
            x.onEvent(HttpServerConfigure.class, e -> {
                e.enableDebug(true);
            });
        });

        initApp(JustSimple.app());
    }

    static void initApp(JustSimpleApp app) {


//        SaManager.getConfig();

        //NamiAttachment.put("lang","en_US");

        //extend: /Users/mutantcat/WORK/work_github/Mutantcat-Working-Group/JustSimple/__test/target/app_ext/
        //System.out.println("extend: " + ExtendLoader.path()+"static");
        System.out.println("extend: " + ExtendLoader.folder());

        System.out.println("testname : " + JustSimple.cfg().get("testname"));


        System.out.println("生在ID = " + CloudClient.id().generate());

        Properties testP = Utils.loadProperties("test.properties");
        if (testP == null) {

        }

//        app.filter((ctx, chain)->{
//            System.out.println("我是过滤器!!!path="+ctx.path());
//            chain.doFilter(ctx);
//        });


//        app.ws("/demof/websocket/{id}",(session,message)->{
//            System.out.println(session.uri());
//            System.out.println("WebSocket-PathVar:Id: " + session.param("id"));
//        });


//        app.ws("/demof/websocket/{id}",(session,message)->{
//            System.out.println(session.uri());
//
//            if(JustSimple.cfg().isDebugMode()){
//                return;
//            }
//
//            if (session.method() == XMethod.WEBSOCKET) {
//                message.setHandled(true);
//
//                session.getOpenSessions().forEach(s -> {
//                    s.send(message.toString());
//                });
//            } else {
//                System.out.println("X我收到了::" + message.toString());
//                //session.send("X我收到了::" + message.toString());
//            }
//        });

        //预热测试
        //PreheatUtils.preheat("/demo1/run0/");

        logger.debug("测试");


        //socket server
        app.router().socketd("/seb/test", (c) -> {
            String msg = c.body();
            c.output("收到了...:" + msg);
        });
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());

        String enc = writer.getEncoding();

        return enc;

    }
}
