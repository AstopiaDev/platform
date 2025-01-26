subprojects {
    group = "net.astopia.backend"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")
    apply(plugin = "java-library")

    if (project.name != "megamind-modules") {
        repositories {
            maven("https://jitpack.io")
        }

        dependencies {
            compileOnly("org.projectlombok:lombok:1.18.36")
            annotationProcessor("org.projectlombok:lombok:1.18.36")

            implementation("org.jetbrains:annotations:24.0.1")
        }
    }
}
