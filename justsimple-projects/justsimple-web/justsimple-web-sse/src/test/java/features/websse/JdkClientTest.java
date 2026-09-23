package features.websse;

import demo.websse.App;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.net.http.HttpUtilsFactory;
import org.mutantcat.justsimple.net.http.impl.jdk.JdkHttpUtilsFactory;
import org.mutantcat.justsimple.net.http.textstream.ServerSentEvent;
import org.mutantcat.justsimple.rx.SimpleSubscriber;
import org.mutantcat.justsimple.test.JustSimpleTest;

import java.util.concurrent.CountDownLatch;

/**
 * @author noear 2025/4/14 created
 */
@Timeout(5)
@JustSimpleTest(App.class)
public class JdkClientTest {
    private HttpUtilsFactory httpUtils = new JdkHttpUtilsFactory();

    @Test
    public void jdk_case1() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        httpUtils.http("http://localhost:" + JustSimple.cfg().serverPort() + "/test/sse1")
                .execAsSseStream("GET")
                .subscribe(new SimpleSubscriber<ServerSentEvent>()
                        .doOnNext(event -> {
                            System.out.println(event);
                            latch.countDown();
                        }).doOnComplete(latch::countDown));
        latch.await();
    }

    @Test
    public void jdk_case2() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        httpUtils.http("http://localhost:" + JustSimple.cfg().serverPort() + "/test/sse2")
                .execAsSseStream("GET")
                .subscribe(new SimpleSubscriber<ServerSentEvent>()
                        .doOnNext(event -> {
                            System.out.println(event);
                            latch.countDown();
                        }).doOnComplete(latch::countDown));
        latch.await();
    }

    @Test
    public void jdk_case3() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);

        httpUtils.http("http://localhost:" + JustSimple.cfg().serverPort() + "/test/sse3")
                .execAsSseStream("GET")
                .subscribe(new SimpleSubscriber<ServerSentEvent>()
                        .doOnNext(event -> {
                            System.out.println(event);
                            latch.countDown();
                        }).doOnComplete(latch::countDown));
        latch.await();
    }
}