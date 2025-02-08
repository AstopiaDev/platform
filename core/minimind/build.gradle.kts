group = "me.vihara.core.minimind"

kotlin {
    jvmToolchain(23)
}

subprojects {
    dependencies {
        implementation(project(":core:minimind"))
        implementation(project(":core:protocol"))
    }
}