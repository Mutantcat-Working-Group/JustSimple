package org.mutantcat.justsimple.shell;


import org.mutantcat.justsimple.JustSimple;

public class AppTest {
    public static void main(String[] args) throws Exception{
        JustSimple.start(AppTest.class, args).block();
    }
}
