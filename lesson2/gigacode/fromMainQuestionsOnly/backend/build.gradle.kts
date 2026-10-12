plugins {
    id("java")
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "ru.otus"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.core:jackson-databind")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}

val flutterWeb by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    flutterWeb("ru.otus:survey_flutter_web:1.0.0")
}

val cleanStatic by tasks.registering(Delete::class) {
    group = "flutter"
    description = "Clean resources/static except .gitkeep"
    
    dependsOn(tasks.named("clean"))
    
    val staticDir = file("src/main/resources/static")
    
    delete(staticDir.listFiles { file -> file.name != ".gitkeep" })
    
    doLast {
        println("✓ Cleaned resources/static (preserved .gitkeep)")
    }
}

val extractFlutterWeb by tasks.registering(Copy::class) {
    group = "flutter"
    description = "Extract Flutter Web zip to resources/static"
    
    dependsOn(cleanStatic)
    
    val zipFile = flutterWeb.resolve().firstOrNull()
        ?: throw GradleException("No flutterWeb artifact found. Run: cd survey_flutter && ./gradlew flutterBuildAndPublish")
    
    from(zipTree(zipFile))
    into(layout.buildDirectory.dir("extracted-flutter-web"))
    
    doLast {
        val staticDir = file("src/main/resources/static")
        staticDir.mkdirs()
        copy {
            from(layout.buildDirectory.dir("extracted-flutter-web"))
            into(staticDir)
        }
        println("✓ Flutter Web extracted to: ${staticDir.absolutePath}")
    }
}

tasks.named("assemble") {
    dependsOn(extractFlutterWeb)
}

tasks.withType<Test> {
    useJUnitPlatform()
}
