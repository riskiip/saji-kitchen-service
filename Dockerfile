# Stage 1: Build the application using Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app

# Copy pom.xml dan download dependencies terlebih dahulu untuk caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy sisa source code
COPY src ./src

# Build aplikasi
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight image using Java 21
FROM openjdk:21-jdk-slim
WORKDIR /app

# Copy hanya file .jar yang sudah di-build dari stage builder
COPY --from=builder /app/target/*.jar app.jar

# Expose port yang digunakan oleh Spring Boot
EXPOSE 8080

# Command untuk menjalankan aplikasi
ENTRYPOINT ["java", "-jar", "app.jar"]