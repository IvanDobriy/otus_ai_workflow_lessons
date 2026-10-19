FROM eclipse-temurin:25-jdk AS build

ARG FLUTTER_VERSION=3.47.2

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates curl git unzip \
    && rm -rf /var/lib/apt/lists/*

RUN git clone --depth 1 --branch "${FLUTTER_VERSION}" https://github.com/flutter/flutter.git /opt/flutter

ENV PATH="/opt/flutter/bin:${PATH}" \
    TAR_OPTIONS="--no-same-owner"

RUN flutter config --no-analytics \
    && flutter precache --web \
    && flutter --version

WORKDIR /workspace
COPY . .

RUN ./gradlew --no-daemon :frontend:publishToMavenLocal \
    && ./gradlew --no-daemon :backend:bootJar


FROM eclipse-temurin:25-jre

RUN groupadd --system app \
    && useradd --system --gid app --no-create-home app

WORKDIR /app
COPY --from=build --chown=app:app /workspace/backend/build/libs/backend-1.0.0.jar /app/app.jar

USER app
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
