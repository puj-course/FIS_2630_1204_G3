# syntax=docker/dockerfile:1
# Imagen de WiseTrip en dos etapas:
#   1) build:   compila el .war con el Maven Wrapper del proyecto (JDK 25)
#   2) runtime: solo el JRE 25 y el .war, para que la imagen final sea liviana

ARG APP_DIR=app/Entrega1/wisetrip_entrega1

# ---------- Etapa 1: compilar ----------
FROM eclipse-temurin:25-jdk AS build
ARG APP_DIR
WORKDIR /build

# Primero el wrapper y el pom: si solo cambia el codigo, Docker reutiliza esta capa
COPY ${APP_DIR}/.mvn .mvn
COPY ${APP_DIR}/mvnw ${APP_DIR}/pom.xml ./
RUN chmod +x mvnw

COPY ${APP_DIR}/src src
# La cache de ~/.m2 evita descargar las dependencias en cada build
RUN --mount=type=cache,target=/root/.m2 ./mvnw -B package -DskipTests

# ---------- Etapa 2: ejecutar ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# No correr la aplicacion como root
RUN useradd --system --no-create-home wisetrip
COPY --from=build /build/target/*.war app.war
USER wisetrip

EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.war"]
