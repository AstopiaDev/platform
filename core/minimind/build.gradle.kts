group = "net.astopia.core.minimind"

kotlin {
    jvmToolchain(23)
}

dependencies {
    implementation(project(":core:protocol"))
}

subprojects {
    dependencies {
        implementation(project(":core:minimind"))
        implementation(project(":core:protocol"))
    }
}