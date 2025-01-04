import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

subprojects {
    dependencies {
        compileOnly(project(":backend:megamind"))
    }

    tasks {
        withType<ShadowJar> {
            val output = project(":backend").ext["output"] as String
            destinationDirectory.set(file("$rootDir/$output"))
        }
    }
}
