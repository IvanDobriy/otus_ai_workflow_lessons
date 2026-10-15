plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("org.openapi.generator") version "7.14.0"
}

description = "Mini-survey backend: Spring WebFlux + OpenAPI generated controllers"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

val generatedOpenApiDir = layout.buildDirectory.dir("generated/openapi")
val openApiSpec = rootProject.layout.projectDirectory.file("protocol/openapi.yaml")

val generateSurveyApi by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    generatorName.set("spring")
    inputSpec.set(openApiSpec.asFile.absolutePath)
    outputDir.set(generatedOpenApiDir.get().asFile.absolutePath)
    apiPackage.set("com.example.survey.api")
    modelPackage.set("com.example.survey.model")
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "reactive" to "true",
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useTags" to "true",
            "skipDefaultInterface" to "true",
            "openApiNullable" to "false",
            "hideGenerationTimestamp" to "true",
            "annotationLibrary" to "none",
            "documentationProvider" to "none",
            "performBeanValidation" to "false",
            "useResponseEntity" to "true",
            "generateSupportingFiles" to "false",
            "useSpringController" to "false"
        )
    )
}

sourceSets {
    main {
        java {
            srcDir(generatedOpenApiDir)
        }
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:4.1.1"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-logging")
    implementation("jakarta.annotation:jakarta.annotation-api")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<JavaCompile>().configureEach {
    dependsOn(generateSurveyApi)
    options.encoding = "UTF-8"
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

val flutterWeb: Configuration by configurations.creating {
    isCanBeConsumed = false
    isCanBeResolved = true
}

dependencies {
    flutterWeb("${project.group}:mini-survey-frontend:${project.version}:web@zip")
}

val staticDir = layout.projectDirectory.dir("src/main/resources/static")

val unpackFlutterWeb by tasks.registering(Sync::class) {
    group = "mini-survey"
    description = "Скачивает Flutter web-артефакт из mavenLocal и распаковывает его в src/main/resources/static"
    dependsOn(":frontend:publishToMavenLocal")
    dependsOn(generateSurveyApi)
    from(Callable { zipTree(flutterWeb.singleFile) })
    into(staticDir)
}

val deleteFlutterWeb by tasks.registering(Delete::class) {
    group = "mini-survey"
    description = "Удаляет распакованный Flutter web-артефакт из src/main/resources/static"
    delete(staticDir)
}

tasks.named("processResources") {
    dependsOn(unpackFlutterWeb)
}

tasks.named("clean") {
    dependsOn(deleteFlutterWeb)
}
