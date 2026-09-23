package org.mutantcat.justsimple.shell;

import org.mutantcat.justsimple.annotation.Component;
import org.mutantcat.justsimple.annotation.Param;
import org.mutantcat.justsimple.shell.annotation.Command;

@Component
public class GreetingCommands {
    @Command(value = "say-hi", description = "简单问候，无参数")
    public String sayHi() {
        return "Hi! 欢迎使用 JustSimple Shell  ～";
    }

    @Command(value = "greet", description = "个性化问候，支持传入姓名（可选，默认：JustSimple）")
    public String greet(
            @Param(defaultValue = "JustSimple", description = "问候对象姓名") String name
    ) {
        return String.format("你好，%s！😀", name);
    }

    @Command(value = "add", description = "整数加法运算，接收两个必选整数参数")
    public String add(
            @Param(required = true, description = "第一个整数") Integer a,
            @Param(required = true, description = "第二个整数") Integer b
    ) {
        return String.format("%d + %d = %d", a, b, a + b);
    }
}

