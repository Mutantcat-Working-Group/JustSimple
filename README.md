<div align="center">
<img src="justsimple_icon.png" style="width:100px;" width="100"/>
<h2>JustSimple</h2>
</div>

JustSimple 是一个基于 Java 的轻量级企业应用开发框架，强调克制、高效、开放。它支持 Java 8 到 Java 26，也可以构建 GraalVM Native Image。

完整说明见 [README_CN.md](README_CN.md)，其他语言见 [English](README_EN.md) | [Русский](README_RU.md) | [日本語](README_JP.md)。

### 引入依赖

当前项目为源码仓库，核心模块坐标为：

```xml
<dependency>
    <groupId>org.mutantcat.justsimple</groupId>
    <artifactId>justsimple</artifactId>
    <version>4.1.1-SNAPSHOT</version>
</dependency>
```

### 快速开始

```java
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.JustSimpleMain;

@JustSimpleMain
public class DemoApp {
    public static void main(String[] args) {
        JustSimple.start(DemoApp.class, args);
    }
}
```

启动后默认通过 `src/main/resources/app.yml` 读取配置，控制器、服务、拦截器等组件按 JustSimple 的注解与包扫描规则注册。

### 模块结构

- `justsimple`：核心框架
- `justsimple-projects`：各功能子模块
- `__test`：项目自测
- `__release`：发布聚合工程
- `justsimple-parent`：统一依赖与构建管理

### 构建与测试

```bash
mvn -B -ntp validate
mvn -B -ntp -pl justsimple -am test
```

更多使用方式请查看 [README_CN.md](README_CN.md)。

### 致谢

感谢 [Solon](https://solon.noear.org) 及其作者们。本项目后期的代码 fork 自 Solon，在此基础上继续演进。
