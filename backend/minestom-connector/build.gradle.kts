import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.1.0"
}

dependencies {
    compileOnly("net.minestom:minestom-snapshots:c976f345d1")

    annotationProcessor(project(":backend:impl-loader"))

    api(project(":backend:base")) {
        exclude(group = "mysql", module = "mysql-connector-java")
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "io.netty", module = "netty-handler")
        exclude(group = "io.netty", module = "netty-transport-native-epoll")
        exclude(group = "io.netty", module = "netty-codec")
        exclude(group = "com.maxmind.geoip2", module = "geoip2")
        exclude(group = "javatar", module = "javatar")
    }

    implementation("org.yaml:snakeyaml:2.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("net.lingala.zip4j:zip4j:2.11.5")

    api(project(":backend:protocol"))
}

tasks {
    withType<ShadowJar> {
        relocate("org.yaml", "net.astopia.yaml")
        relocate("com.google.gson", "net.astopia.gson")
        val output = project(":backend").extensions.extraProperties["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))
        archiveFileName.set("MinestomConnector.jar")
    }
}

kotlin {
    jvmToolchain(23)
}
