import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer

dependencies {
    annotationProcessor(project(":backend:impl-loader"))

    api(project(":backend:base"))
    implementation("mysql:mysql-connector-java:8.0.33") // 5.1.48

    implementation("com.github.simplix-softworks:simplixstorage:3.2.6")
    implementation("net.lingala.zip4j:zip4j:2.11.5")

    api(project(":backend:protocol"))
}

tasks {
    withType<ShadowJar> {
        transform(Log4j2PluginsCacheFileTransformer())

        val output = project(":backend").extensions.extraProperties["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))

        archiveFileName.set("BukkitStartupApp.jar")

        manifest {
            attributes(
                    mapOf(
                            "Main-Class" to "net.astopia.backend.bukkit.startup.BukkitAgentBootstrapper"
                    )
            )
        }
    }
}
