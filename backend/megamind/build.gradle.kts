import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

dependencies {
    api(project(":backend:base"))
    api(project(":backend:protocol"))
    api(project(":backend:megamind-api"))

    implementation("com.google.code.gson:gson:2.11.0")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:2.20.0")

    api("org.jline:jline-reader:3.26.3")
    api("org.jline:jline-terminal:3.26.3")
    api("org.jline:jline-terminal-jansi:3.26.3")
    api("net.minecrell:terminalconsoleappender:1.3.0")

    api("org.apache.logging.log4j:log4j-core:2.20.0")
    api("org.apache.logging.log4j:log4j-iostreams:2.20.0")

    api("commons-cli:commons-cli:1.3.1")

    api(project(":backend:impl-loader"))
    annotationProcessor(project(":backend:impl-loader"))
}

repositories {
    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

tasks {
    withType<ShadowJar> {
        transform(Log4j2PluginsCacheFileTransformer())

        val output = project(":backend").extensions.extraProperties["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))

        archiveFileName.set("MegamindApp.jar")

        manifest {
            attributes(
                    "Main-Class" to "net.skykingdoms.backend.megamind.boot.MegamindBoot"
            )
        }
    }
}
