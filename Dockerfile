# JustSimple 构建镜像：容器内执行 Maven 构建/测试。
FROM maven:3.9-eclipse-temurin-17

ARG VERSION=1.0.20260920

WORKDIR /workspace

COPY . .

ENV MAVEN_OPTS="-Dmaven.repo.local=/workspace/.m2"

ENTRYPOINT ["mvn", "-B", "-ntp"]
CMD ["package"]
