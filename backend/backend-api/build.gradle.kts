plugins {
    kotlin("jvm") version "2.1.0"
}

dependencies {
    compileOnly(project(":backend:protocol-api"))
    implementation("com.google.code.gson:gson:2.11.0")

    // @DEPRECATED
    // TODO: migrate to fast util
    api("it.unimi.dsi:fastutil:8.1.0")
    api("net.sf.trove4j:trove4j:3.0.3")
    api("com.google.guava:guava:33.2.1-jre")
    api("org.yaml:snakeyaml:2.0")
    api("net.kyori:adventure-text-minimessage:4.18.0")
    api("net.kyori:adventure-api:4.18.0")
    api("net.kyori:adventure-text-serializer-plain:4.18.0")
    api("net.kyori:adventure-text-serializer-gson:4.18.0")

    api(project(":backend:impl-loader"))
    annotationProcessor(project(":backend:impl-loader"))
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(23)
}
