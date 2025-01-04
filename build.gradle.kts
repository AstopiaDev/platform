import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.kotlin.dsl.repositories
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.JavaVersion

plugins {
    id("com.gradleup.shadow") version "9.0.0-beta4" apply false
    id("java")
}

val user by extra(findProperty("repo.skykingdoms.user")?.toString() ?: error("Missing repository user"))
val pass by extra(findProperty("repo.skykingdoms.pass")?.toString() ?: error("Missing repository password"))
val buildDirectory = file("./build/")

subprojects {
    extra["output"] = findProperty("gradle.output") ?: "./output/"

    group = "net.skykingdoms"
    version = "0.0.1-SNAPSHOT"

    val customBuildDir: DirectoryProperty = project.layout.buildDirectory
    customBuildDir.set(file("$buildDirectory/${project.name}"))

    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "java")

    repositories {
        mavenCentral()

        listOf(
            "https://repo.skykingdoms.net/repository/maven-snapshots/",
            "https://repo.skykingdoms.net/repository/maven-releases/"
        ).forEach { repoUrl ->
            maven {
                url = uri(repoUrl)
                credentials {
                    username = user
                    password = pass
                }
            }
        }
    }

    tasks {
        withType<ShadowJar> {
            exclude("META-INF/DEPENDENCIES")
            exclude("META-INF/LICENSE")
            exclude("META-INF/LICENSE.txt")
            exclude("META-INF/NOTICE")
            exclude("META-INF/NOTICE.txt")
            exclude("META-INF/maven/**")

            mergeServiceFiles {
                include("META-INF/impl/**")
            }
        }

        withType<JavaCompile> {
            targetCompatibility = JavaVersion.VERSION_23.toString()
            sourceCompatibility = JavaVersion.VERSION_23.toString()
            options.encoding = "UTF-8"
        }

        named("build") {
            dependsOn("shadowJar")
        }
    }
}
