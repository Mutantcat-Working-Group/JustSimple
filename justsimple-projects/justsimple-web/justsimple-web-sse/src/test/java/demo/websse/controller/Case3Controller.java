package demo.websse.controller;

import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Mapping;
import org.mutantcat.justsimple.core.handle.Entity;
import org.mutantcat.justsimple.rx.handle.RxEntity;
import org.mutantcat.justsimple.web.sse.SseEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author noear 2025/12/16 created
 *
 */
@Mapping("case3")
@Controller
public class Case3Controller {
    @Mapping("m1")
    public Mono<Entity> m1(String name) {
        return RxEntity.ok().body("Hello " + name);
    }

    @Mapping("m2")
    public Mono<Entity> m2(String name) {
        return RxEntity.ok().body(Mono.just("Hello " + name));
    }

    @Mapping("f1")
    public Mono<Entity> f1(String name) {
        return RxEntity.ok().body(new SseEvent().data("test"));
    }

    @Mapping("f2")
    public Mono<Entity> f2(String name) {
        return RxEntity.ok().body(Flux.just(new SseEvent().data("test1"),
                new SseEvent().data("test2")));
    }
}