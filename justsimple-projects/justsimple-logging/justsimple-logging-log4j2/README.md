
## 使用说明

### 1、内部署默认配置（当什么都不加时，以此配置运行）

```yaml
justsimple.app:
  name: demoapp

# 以下为默认值，可以都不加，或者想改哪行加哪行(支持"云端配置服务"进行配置，支持写到"云端日志服务")
justsimple.logging.appender:
  console:
    level: TRACE
    pattern: "%highlight{%-5level %d{yyyy-MM-dd HH:mm:ss.SSS} [-%t][*%X{traceId}]%tags[%logger{20}]:} %n%msg%n"
  file:
    name: "logs/${justsimple.app.name}"
    extension: ".log"
    level: INFO
    pattern: "%-5level %d{yyyy-MM-dd HH:mm:ss.SSS} [-%t][*%X{traceId}]%tags[%logger{20}]: %n%msg%n"
  cloud:
    level: INFO

# 记录器级别的配置示例
justsimple.logging.logger:
  "root": #默认记录器配置
    level: INFO
  "features":
    level: DEBUG
  "org.jetty.demo":
    level: WARN
```

### 2、如果想高度定制，自定义可 xml 配置（支持环境切换）
```yaml
#默认配置，可以从插件里复制 log4j2-def.xml 进行修改（-justsimple 可以支持 justsimple 特性）
log4j2-justsimple.xml

#环镜配置
log4j2-justsimple-{env}.xml 
```

也可以用 `log4j2.xml` 配置文件。但其它配置都会失效，也没有环境切换功能
