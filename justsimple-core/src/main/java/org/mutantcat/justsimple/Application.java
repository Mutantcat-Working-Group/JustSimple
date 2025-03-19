package org.mutantcat.justsimple;

import org.mutantcat.justsimple.starter.ApplicationStarter;

public class Application {
    public static void start(Class<?> clazz,String[] args) {
        ApplicationStarter starter = new ApplicationStarter();
        starter.start(clazz,args);
    }
}