package features.enjoy;

import org.mutantcat.justsimple.annotation.Component;

@Component
public class HelloService {
    public String greet(String name) {
        return "Hi, " + name + "!";
    }
}
