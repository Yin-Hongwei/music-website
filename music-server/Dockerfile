# Build from source (CI / compose). Config via SPRING_PROFILES_ACTIVE + env vars.
FROM maven:3-openjdk-8-slim AS builder
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:resolve -DskipTests -B

COPY src ./src
RUN mvn clean package -DskipTests -B -T 1C

FROM openjdk:8-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/yin-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/app.jar"]
