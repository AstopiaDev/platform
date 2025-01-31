import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

subprojects {
    dependencies {
        compileOnly(project(":backend:megamind"))
        implementation("org.reflections:reflections:0.10.2")
    }

    tasks {
        withType<ShadowJar> {
            val output = project(":backend").ext["output"] as String
            destinationDirectory.set(file("$rootDir/$output"))
        }
    }
}
