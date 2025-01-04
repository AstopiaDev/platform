import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.0"
}

val output: String = findProperty("output.path")?.toString()
    ?: project(":backend").ext["output"] as String

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":api:lobby"))
}

tasks {
    named<Jar>("jar") {
        manifest {
            attributes["Main-Class"] = "MainLobbyServerKt"
        }
    }

    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("server")
        archiveClassifier.set("")
        archiveVersion.set("")
        destinationDirectory.set(file(output))
    }
}
