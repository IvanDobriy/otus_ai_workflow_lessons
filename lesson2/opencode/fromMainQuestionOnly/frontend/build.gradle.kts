plugins {
    base
    `maven-publish`
    id("org.openapi.generator") version "7.14.0"
}

description = "Mini-survey frontend: Flutter Web (Dart client сгенерирован из OpenAPI)"

val appDir = layout.projectDirectory.dir("app")
val generatedApiDir = appDir.dir("generated_api")
val openApiSpec = rootProject.layout.projectDirectory.file("protocol/openapi.yaml")

val generateSurveyApi by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    generatorName.set("dart")
    inputSpec.set(openApiSpec.asFile.absolutePath)
    outputDir.set(generatedApiDir.asFile.absolutePath)
    configOptions.set(
        mapOf(
            "pubName" to "survey_api",
            "pubVersion" to "1.0.0",
            "pubDescription" to "Dart client для протокола Mini Survey API",
            "dateLibrary" to "core",
            "useEnumExtension" to "true"
        )
    )
    doLast {
        // Генерируемый dart-клиент импортирует dart:io ради HttpStatus и типов
        // SocketException/TlsException/IOException, что несовместимо со сборкой
        // под web. После генерации убираем dart:io и подкладываем совместимый шим.
        val libDir = generatedApiDir.asFile.resolve("lib")
        val apiFile = libDir.resolve("api.dart")
        if (apiFile.exists()) {
            libDir.resolve("io_shims.dart").writeText(
                """
                |// @dart=2.18
                |//
                |// Совместимый с web шим вместо символов dart:io, которые тянет
                |// сгенерированный dart-клиент.
                |//
                |part of openapi.api;
                |
                |class HttpStatus {
                |  static const int noContent = 204;
                |  static const int badRequest = 400;
                |  static const int internalServerError = 500;
                |}
                |
                |class SocketException implements Exception {
                |  final String message;
                |  SocketException(this.message);
                |  @override
                |  String toString() => 'SocketException: ${'$'}message';
                |}
                |
                |class TlsException implements Exception {
                |  final String message;
                |  TlsException(this.message);
                |  @override
                |  String toString() => 'TlsException: ${'$'}message';
                |}
                |
                |class IOException implements Exception {
                |  final String message;
                |  IOException(this.message);
                |  @override
                |  String toString() => 'IOException: ${'$'}message';
                |}
                |""".trimMargin()
            )
            val content = apiFile.readText()
                .replace("import 'dart:io';\n", "")
                .replace(
                    "part 'api/survey_api.dart';",
                    "part 'io_shims.dart';\npart 'api/survey_api.dart';"
                )
            apiFile.writeText(content)
        }
    }
}

val cleanFlutterApp by tasks.registering(Delete::class) {
    description = "Удаляет артефакты сборки Flutter-приложения (app/build, app/.dart_tool)"
    delete(appDir.dir("build"), appDir.dir(".dart_tool"))
}

tasks.named("clean") {
    dependsOn(cleanFlutterApp)
}

val flutterPubGet by tasks.registering(Exec::class) {
    group = "mini-survey"
    description = "Скачивает зависимости Flutter-приложения"
    dependsOn(generateSurveyApi)
    workingDir = appDir.asFile
    commandLine("flutter", "pub", "get")
    inputs.file(appDir.file("pubspec.yaml"))
    outputs.file(appDir.file("pubspec.lock"))
    outputs.file(appDir.file(".dart_tool/package_config.json"))
}

val flutterBuildWeb by tasks.registering(Exec::class) {
    group = "mini-survey"
    description = "Собирает Flutter Web приложение (release)"
    dependsOn(flutterPubGet)
    workingDir = appDir.asFile
    commandLine("flutter", "build", "web", "--release")
    inputs.dir(appDir.dir("lib"))
    inputs.dir(generatedApiDir)
    inputs.file(appDir.file("pubspec.yaml"))
    inputs.file(appDir.file("web/index.html"))
    outputs.dir(appDir.dir("build/web"))
}

val flutterWebZip by tasks.registering(Zip::class) {
    group = "mini-survey"
    description = "Упаковывает собранное Flutter Web приложение в zip-артефакт"
    dependsOn(flutterBuildWeb)
    from(appDir.dir("build/web"))
    archiveFileName.set("mini-survey-frontend-${project.version}-web.zip")
    destinationDirectory.set(layout.buildDirectory.dir("distributions"))
}

publishing {
    publications {
        create<MavenPublication>("flutterWeb") {
            groupId = "com.example.survey"
            artifactId = "mini-survey-frontend"
            version = project.version.toString()
            artifact(flutterWebZip) {
                classifier = "web"
                extension = "zip"
            }
        }
    }
}

tasks.named("assemble") {
    dependsOn(flutterWebZip)
}
