FROM openjdk:17-jdk-slim-buster as maven

WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests

# For Java 11,
FROM openjdk:17-jdk-slim-buster

WORKDIR /app
COPY --from=maven /app/target/yandex-0.0.1-SNAPSHOT.jar /app
ENTRYPOINT ["java","-jar","yandex-0.0.1-SNAPSHOT.jar"]
