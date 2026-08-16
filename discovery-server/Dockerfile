FROM maven:3.9.11-amazoncorretto-17 AS build

WORKDIR /app

COPY discovery-server/pom.xml .
COPY discovery-server/src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre

WORKDIR /app

RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8761

ENTRYPOINT ["java", "-jar", "app.jar"]