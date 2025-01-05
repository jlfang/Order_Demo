FROM maven:3.6.3-openjdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8
COPY --from=build /home/app/target/order-demo-1.0.0.jar /usr/local/lib/order-demo.jar
ENTRYPOINT ["java", "-jar", "/usr/local/lib/order-demo.jar"]