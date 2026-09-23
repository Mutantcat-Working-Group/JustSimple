package features.enjoy;

import org.mutantcat.justsimple.annotation.Component;

@Component("share:greeting")
public class Greeting {
    public String greet(String name) {
        return "Hi, " + name + "!";
    }
}
