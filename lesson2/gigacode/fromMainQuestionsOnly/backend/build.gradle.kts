plugins {
    id("java")
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
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

val flutterWeb by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}


dependencies {
    flutterWeb("ru.otus:survey_flutter_web:1.0.0")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("net.logstash.logback:logstash-logback-encoder:7.4")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
}



val cleanStatic by tasks.registering(Delete::class) {
    group = "flutter"
    description = "Clean resources/static except .gitkeep"
    
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
    into(layout.projectDirectory.dir("src/main/resources/static"))
    
    doLast {
        val staticDir = file("src/main/resources/static")
//        staticDir.mkdirs()
//        copy {
//            from(layout.buildDirectory.dir("extracted-flutter-web"))
//            into(staticDir)
//        }
        println("✓ Flutter Web extracted to: ${staticDir.absolutePath}")
    }
}


tasks.withType<Test> {
    useJUnitPlatform()
}
