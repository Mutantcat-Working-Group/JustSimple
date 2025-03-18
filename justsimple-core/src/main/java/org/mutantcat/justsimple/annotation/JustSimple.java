package org.mutantcat.justsimple.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// 用于标记驱动类
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface JustSimple {
    String packageName() default ""; // 包名
}