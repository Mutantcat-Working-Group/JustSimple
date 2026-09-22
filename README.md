<div align=center>
<img src="https://s2.loli.net/2025/03/19/Gf9yaFAJmnVWIBH.png" style="width:100px;" width="100"/>
<h2>仅见</h2>
</div>

### 一、产品概述

- 非常非常简单的 Java Web Api 开发库，一分钟上手开发 Api 接口。
- 适合其他语言转码直接上手使用，大程度简化各种操作。
- 可作为本地测试、简单接口、快速原型、极限编程、临时模型等场景的底座。
- 基于 Netty，注解驱动，无繁重配置，生态仍在持续扩充。

核心价值：一个注解启动、一个上下文对象收参数，写接口只剩业务代码。

### 二、功能说明

- 注解驱动：`@JustSimple` 启动，`@Controller` + `@Handler` 定义路由，`@Instance` 自动注册实例。
- 上下文对象：`Context` 一次性拿到 GET 参数、POST JSON、表单数据与原始 `FullHttpRequest`。
- 实例池：通过 `InstanceHandler` 存取单例，Controller 可随时取用。
- 双配置方式：启动前代码配置或 `@Instance` 自动注册，也可走 `application.yaml` 配置文件。
- CORS 内置：开箱可配的跨域规则，支持任意来源、方法、请求头与预检缓存。

### 三、安装与下载

Maven 引入（当前版本 `1.0.20260920`）：

```xml
<dependencies>
    <dependency>
        <groupId>org.mutantcat.justsimple</groupId>
        <artifactId>justsimple-core</artifactId>
        <version>1.0.20260920</version>
    </dependency>
</dependencies>
```

也可从 [Releases](https://github.com/Mutantcat-Working-Group/JustSimple/releases) 下载源码包（最新 `v1.0.20260921`），另附 `checksums.txt` 供校验。版本号使用纯日期递增，推送同族标签（`v` 前缀可选）后，GitHub Actions 会自动打包并发布 Release。

### 四、快速上手

#### 启动类

```java
import org.mutantcat.justsimple.Application;
import org.mutantcat.justsimple.annotation.JustSimple;

@JustSimple // 可指定packageName为基础包包名
public class Main {
    public static void main(String[] args) {
        // 一句话启动
        Application.start(Main.class,args);
    }
}
```

#### 控制器定义

```java
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Handler;
import org.mutantcat.justsimple.request.Context;

@Controller
public class MyController {
    @Handler(path = "/hello")
    public String hello(Context ctx) {
        // 获得get请求携带的params参数
        List<String> name = ctx.getParam("name");
        // 获得post请求携带的json
        String json = ctx.getJson()
        // 获得post请求携带的表单
        Map<String, Object> formData = ctx.getFormData();
        // 获得fullHttpRequest
        FullHttpRequest fullHttpRequest = ctx.getFullHttpRequest();
        return "Hello, JustSimple!";
    }
}
```

#### 实例定义与存取

```java
import org.mutantcat.justsimple.annotation.Instance;

@Instance
public class MyInstance {
    public String hello() {
        return "Hello, JustSimple!";
    }
}
```

```java
InstanceHandler.putInstance("MyInstance", this);
InstanceHandler.getInstance("MyInstance");
```

### 五、开发者集成

#### 直接添加配置

```java
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import org.mutantcat.justsimple.instance.InstanceHandler;

public class ConfigHandler {
    // 之后需要在启动方法前调用args = ConfigHandler.doConfig(args);
    public static String[] doConfig(String[] args) {
        // 配置信息 可以存入一个配置类的 当然这个配置实例也可以使用@Instance注解自动注册
        CorsConfig corsConfig = CorsConfigBuilder.forAnyOrigin() // 允许任意来源
                .allowNullOrigin() // 允许空的来源
                .allowedRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE) // 允许的方法
                .allowedRequestHeaders("*") // 允许任意请求头
                .exposeHeaders("X-Custom-Header") // 暴露自定义的响应头
                .maxAge(3600) // 设置预检请求的缓存时间（单位秒）
                .build();
        InstanceHandler.putInstance("CorsConfigInstanceName",corsConfig);

        Integer port = 8082;
        InstanceHandler.putInstance("portConfigName",port);

        // 新的参数 格式用于指定配置类的实例名称
        String[] newParams = {"corsConfig:CorsConfigInstanceName","port:portConfigName"};

        // 合并原参数与新参数
        String[] updatedArgs = new String[args.length + newParams.length];
        System.arraycopy(args, 0, updatedArgs, 0, args.length);
        System.arraycopy(newParams, 0, updatedArgs, args.length, newParams.length);
        return updatedArgs;
    }
}
```

#### 通过实例添加配置

```java
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import org.mutantcat.justsimple.annotation.Instance;
import org.mutantcat.justsimple.instance.InstanceHandler;

@Instance
public class CorsConfig {

    public CorsConfig() {
        // 这样调用实例的构造方法，会将配置实例放入实例池中
        InstanceHandler.putInstance("CorsConfigInstanceName",  CorsConfigBuilder.forAnyOrigin() // 允许任意来源
                .allowNullOrigin() // 允许空的来源
                .allowedRequestMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE) // 允许的方法
                .allowedRequestHeaders("*") // 允许任意请求头
                .exposeHeaders("X-Custom-Header") // 暴露自定义的响应头
                .maxAge(3600) // 设置预检请求的缓存时间（单位秒）
                .build());
    }
}
```

#### 配置列表

| 配置名称 | 实例类型 | 配置说明 |
| --- | --- | --- |
| `corsConfig:配置实例名` | io.netty.handler.codec.http.cors.CorsConfig | 跨域规则配置 |
| `port:配置实例名` | java.lang.Integer | 程序运行端口 |

#### 其他说明

- `resource` 下的 `application.yaml` 就是配置文件。
- Controller 必须放到 `controller` 包下（强制规范）。
- 配置格式为 `corsConfig:CorsConfigInstanceName`、`port:portConfigName`，只需将配置传入 `args`；`:` 前是固定配置名，`:` 后是注册的实例名，英文冒号。
- 代码配置优先级大于配置文件配置，自动注册的 Instance 优先级大于启动语句前配置的。
- 想用单例 Controller，同时添加 `@Instance` 和 `@Controller` 两个注解即可。
- `@Instance` 的常用用法不是当构造方法调用器，而是把注解类的实例自动注册到实例池，可用 `name` 参数指定注册名（默认类路径）。
- 样例：https://github.com/tyza66/JustSimpleDemo
- 插件：https://github.com/Mutantcat-Working-Group/JustSimple/tree/main/docs

### 六、开源协议

本项目基于 Apache-2.0 协议开源。
