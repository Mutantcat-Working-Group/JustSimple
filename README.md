<div align=center>
<img src="https://s2.loli.net/2025/03/19/Gf9yaFAJmnVWIBH.png" style="width:100px;"/>
<h2>仅见</h2>
</div>

### 一、产品概述

- 非常非常简单的Java Web Api开发库，一分钟上手开发Api接口
- 适合其他语言转码直接上手使用，大程度简化各种操作
- 可以作为本地测试、简单接口、快速原型、极限编程、临时模型等
- 生态仍在持续扩充中，感谢各国产框架提供的兼容与支持

### 二、引入依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.mutantcat.justsimple</groupId>
        <artifactId>justsimple-core</artifactId>
        <version>1.0.20250321</version>
    </dependency>
</dependencies>
```

### 三、启动类

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

### 四、控制器定义

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

### 五、实例定义

```java
import org.mutantcat.justsimple.annotation.Instance;

@Instance
public class MyInstance {
    public String hello() {
        return "Hello, JustSimple!";
    }
}
```

### 六、实例存取

```java
InstanceHandler.putInstance("MyInstance", this);
InstanceHandler.getInstance("MyInstance");
```

### 七、直接添加配置

```java
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsConfigBuilder;
import org.mutantcat.justsimple.instance.InstanceHandler;

public class ConfigHandler {
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

### 八、通过实例添加配置

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

### 九、配置列表

| 配置名称              | 实例类型                                    | 配置说明     |
| --------------------- | ------------------------------------------- | ------------ |
| corsConfig:配置实例名 | io.netty.handler.codec.http.cors.CorsConfig | 跨域规则配置 |
| port:配置实例名       | java.lang.Integer                           | 程序运行端口 |

### 八、其他说明

- resourse下的application.yaml就是配置文件
- Controller必须放到controller包下（强制规范）
- 配置格式是corsConfig:CorsConfigInstanceName、port:portConfigName
- 只需将配置传入args中即可，注意配置的时候:前第一个是固定的配置名，:后第二个是注册的实例名
- 注意是英文冒号，普通的配置的Object类型要与列表中对应
- 代码配置优先级大于配置文件配置，自动注册的Instance优先级大于启动语句前配置的



