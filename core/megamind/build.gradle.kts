group = "net.astopia.core.megamind"

dependencies {
    implementation(project(":core:protocol"))
    implementation(project(":core:minimind"))
}

kotlin {
    jvmToolchain(23)
}