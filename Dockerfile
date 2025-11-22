# ===== STAGE 1: build =====
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copia só o pom primeiro (melhora cache)
COPY pom.xml .

# Baixa dependências (opcional, mas acelera builds)
RUN mvn -B -e dependency:go-offline

# Agora copia o código
COPY src ./src

# Builda o jar
RUN mvn -B -e clean package -DskipTests

# ===== STAGE 2: runtime =====
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# copia o jar gerado do stage de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]