FROM maven:3.8.3-openjdk-17 AS builder
ADD ./pom.xml pom.xml
ADD ./src src/
RUN mvn clean package -Dmaven.test.skip=true

FROM amazoncorretto:17.0.8-alpine
#WORKDIR /app
EXPOSE 8003
COPY --from=builder target/payments*.jar payments.jar
ENTRYPOINT ["java", "-jar", "payments.jar"]