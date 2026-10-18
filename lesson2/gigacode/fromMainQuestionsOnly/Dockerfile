FROM ghcr.io/cirruslabs/flutter:stable AS flutter-build

WORKDIR /workspace
COPY survey_flutter/pubspec.yaml survey_flutter/pubspec.lock ./
RUN flutter pub get
COPY survey_flutter/ ./
RUN flutter build web --release

FROM eclipse-temurin:21-jdk AS backend-build

WORKDIR /workspace
COPY backend/ ./
COPY --from=flutter-build /workspace/build/web ./src/main/resources/static
RUN ./gradlew --no-daemon clean build

FROM eclipse-temurin:21-jre

WORKDIR /app
COPY --from=backend-build /workspace/build/libs/mini-survey-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
