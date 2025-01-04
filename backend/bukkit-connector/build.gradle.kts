import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    compileOnly("net.skykingdoms:spigot:1.8.8-R0.1")

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

    api(project(":backend:protocol"))
}

tasks {
    withType<ShadowJar> {
        relocate("org.yaml", "net.skykingdoms.yaml")
        relocate("com.google.gson", "net.skykingdoms.gson")
        val output = project(":backend").extensions.extraProperties["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))
        archiveFileName.set("BukkitConnector.jar")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(23))
    }
}
