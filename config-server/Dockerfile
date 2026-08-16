FROM maven:3.9.11-amazoncorretto-17 AS build

WORKDIR /app

COPY config-server/pom.xml .
COPY config-server/src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre

WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

COPY config-repo /config-repo

EXPOSE 8888

ENTRYPOINT ["java", "-jar", "app.jar"]