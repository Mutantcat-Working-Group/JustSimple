## justsimple-gradle-plugin

#### 1.打包插件配置示例

在 `build.gradle` 中使用

```groovy
buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        classpath 'org.mutantcat.justsimple:justsimple-gradle-plugin:x.y.z'
    }
}


// 使用
apply plugin: 'org.mutantcat.justsimple'


compileJava {
    // 这两个配置可以不添加了，插件中会默认自动添加
    options.encoding = "UTF-8"
    options.compilerArgs << "-parameters"
}

// 配置启动文件名
justsimple {
    mainClass = "com.example.demo.App"
}

// 也可以针对 jar包和 war包指定不同的 mainClass

justsimpleJar{
    mainClass = "com.example.demo.App"
}

// 使用 justsimpleWar 需要添加 war 插件
justsimpleWar{
    mainClass = "com.example.demo.App"
}

```

在 `build.gradle.kts` 中使用

```kotlin
buildscript {
    repositories {
        mavenLocal()
        maven { setUrl("https://maven.aliyun.com/repository/public") }
        mavenCentral()
    }

    dependencies {
        classpath("org.mutantcat.justsimple:justsimple-gradle-plugin:x.y.z")
    }
}

// 引用插件
apply(plugin = "org.mutantcat.justsimple")

// 统一配置
extensions.configure(org.mutantcat.justsimple.gradle.dsl.JustSimpleExtension::class.java) {
    mainClass.set("com.example.demo.App")
}

// 单独配置
tasks.withType<org.mutantcat.justsimple.gradle.tasks.bundling.JustSimpleJar> {
    mainClass.set("com.example.demo.App")
}

tasks.withType<org.mutantcat.justsimple.gradle.tasks.bundling.JustSimpleWar> {
    mainClass.set("com.example.demo.App")
}

```

#### 2. 构建打包

* `gradle justsimpleJar`
* `gradle justsimpleWar`

#### 3. 更新
* `0.0.2` 
  
  1.  修复未 `build`情况下直接 运行  `gradle justsimpleJar` 报错问题
  
  2. 自动扫描 `main` 方法，但是如果有多个的时候仍然需要手动配置，~~可自行新增注解类`org.mutantcat.justsimple.annotation.JustSimpleMain`~~(justsimple 2.2开始自带此类)，将注解添加到启动类上
  
    ```java
    package org.mutantcat.justsimple.annotation;
    
    import java.lang.annotation.*;
    
    /**
     * JustSimple 主类（入口类）
     *
     * <pre>{@code
     * @JustSimpleMain
     * public class App{
     *     public static void main(String[] args){
     *         JustSimple.start(App.class, args);
     *     }
     * }
     * }</pre>
     *
     * @author noear
     * @since 2.2
     * */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface JustSimpleMain {
    
    }
    
    // 启动类上添加
    
    @JustSimpleMain
    public class App {
        public static void main(String[] args) {
            JustSimple.start(App.class, args);
        }
    }
    ```
  
    
