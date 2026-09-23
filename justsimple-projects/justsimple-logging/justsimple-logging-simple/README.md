
* 实现 slf4j 接口对接，为分布式日志服务提供入口.
* justsimple.cloud 不再直接依赖 justsimple.logging.impl


日志服务将由：

* log4j2-justsimple-plugin //本地日志
* logback-justsimple-plugin  //本地日志
* water-justsimple-plugin //分布式日志服务