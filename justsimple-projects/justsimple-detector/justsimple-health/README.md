<h1 align="center">JustSimple Health Checks</h1>

<div align="center">
Author noear，iYarnFog
</div>

## ✨ 特性

- 🌈 一行代码添加检查点，侵入性极低。
- 📦 开箱即用的高质量组件。

## 📦 安装

```xml
<dependency>
    <groupId>org.noear</groupId>
    <artifactId>justsimple-health</artifactId>
</dependency>
```

## ⚙️ 配置

```yaml
# No configuration.
```

## 🔨 示例

```java
@Configuration
public class Config {
    @Managed
    public void initHealthCheckPoint() {
        //test...
        HealthChecker.addIndicator("preflight", Result::succeed);
        HealthChecker.addIndicator("test", Result::failure);
        HealthChecker.addIndicator("boom", () -> {
            throw new IllegalStateException();
        });
    }
}
```

```text
GET /healthz
Response Code:
200 : Everything is fine
503 : At least one procedure has reported a non-healthy state
500 : One procedure has thrown an error or has not reported a status in time
Response:
{"status":"ERROR","details":{"test":{"status":"DOWN"},"boom":{"status":"ERROR"},"preflight":{"status":"UP","details":{"total":987656789,"free":6783,"threshold":7989031}}}}
```