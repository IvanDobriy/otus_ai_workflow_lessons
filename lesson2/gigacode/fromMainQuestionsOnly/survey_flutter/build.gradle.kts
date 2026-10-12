plugins {
    java
    `maven-publish`
}

group = "ru.otus"
version = "1.0.0"

val flutterHome = project.findProperty("flutter.home") as? String
    ?: System.getenv("FLUTTER_HOME")
    ?: System.getProperty("user.home") + "/Documents/Applications/flutter_linux_3.47.2-stable/flutter"
val flutterScript = file("$flutterHome/bin/flutter")

val buildWebDir = layout.buildDirectory.dir("web")
val zipArchive = layout.buildDirectory.file("archives/survey_flutter_web-${version}.zip")

val flutterBuild by tasks.registering(Exec::class) {
    group = "flutter"
    description = "Build Flutter Web application"
    commandLine = listOf("bash", "-c", "export PATH=$flutterHome/bin:\$PATH && flutter build web --release")
    workingDir = projectDir
    
    doLast {
        println("✓ Flutter web build completed: ${buildWebDir.get().asFile}")
    }
}

val zipFlutterWeb by tasks.registering(Zip::class) {
    group = "flutter"
    description = "Create zip archive of Flutter Web build"
    dependsOn(flutterBuild)
    
    archiveFileName.set("survey_flutter_web-${version}.zip")
    destinationDirectory.set(layout.buildDirectory.dir("archives"))
    
    from(buildWebDir)
    
    doLast {
        println("✓ Zip archive created: ${zipArchive.get().asFile}")
    }
}

publishing {
    publications {
        create<MavenPublication>("flutterWebZip") {
            artifact(zipFlutterWeb.map { it.archiveFile })
            artifactId = "survey_flutter_web"
            groupId = project.group.toString()
            version = project.version.toString()
            
            pom {
                name.set("Survey Flutter Web")
                description.set("Flutter Web build artifact for Mini Survey application")
                url.set("https://github.com/otus/mini-survey")
            }
        }
    }
    
    repositories {
        maven {
            url = file("${System.getProperty("user.home")}/.m2/repository").toURI()
        }
    }
}

tasks.register("flutterBuildAndPublish") {
    group = "flutter"
    description = "Build Flutter, create zip, and publish to maven local"
    dependsOn(zipFlutterWeb, "publishFlutterWebZipPublicationToMavenRepository")
    
    doLast {
        println("\n=== Flutter Build & Publish Complete ===")
        println("Archive: ${zipArchive.get().asFile.absolutePath}")
        println("Maven local: ${file("${System.getProperty("user.home")}/.m2/repository")}")
    }
}
