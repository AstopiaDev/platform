import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

dependencies {
    api(project(":backend:base"))
    api(project(":backend:protocol"))

    api("com.github.docker-java:docker-java-core:3.4.0")
    api("com.github.docker-java:docker-java-transport-httpclient5:3.4.0")
    api("org.apache.logging.log4j:log4j-core:2.20.0")
    api("org.apache.logging.log4j:log4j-iostreams:2.20.0")

    api(project(":backend:impl-loader"))
    annotationProcessor(project(":backend:impl-loader"))
}

tasks {
    withType<ShadowJar> {
        transform(Log4j2PluginsCacheFileTransformer())

        val output = project(":backend").extensions.extraProperties["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))

        archiveFileName.set("NodeApp.jar")

        manifest {
            attributes(
                "Main-Class" to "net.skykingdoms.backend.node.boot.NodeBoot"
            )
        }
    }
}

repositories {
    mavenCentral()
}
