import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

tasks {
    withType<ShadowJar> {
        archiveFileName.set("MessageChannel-Megamind.jar")
    }
}
