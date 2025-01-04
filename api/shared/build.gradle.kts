dependencies {
    api("net.minestom:minestom-snapshots:2065f63f82")
    api("com.github.luben:zstd-jni:1.5.6-9")
    api(project(":backend:minestom-connector"))
    api(project(":backend:base"))
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}