FROM maven:3.6.3-openjdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# 使用阿里云镜像仓库地址
FROM registry.cn-hangzhou.aliyuncs.com/library/openjdk:8-jdk-alpine
COPY --from=build /home/app/target/order-demo-1.0.0.jar /usr/local/lib/order-demo.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/order-demo.jar"]