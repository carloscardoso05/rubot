# Stage 1: Build the application
FROM eclipse-temurin:24-jdk-alpine AS build

# Instala o Maven (já que mvnw não vai estar disponível)
RUN apk add --no-cache maven

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:24-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
