plugins {
    kotlin("jvm") version "2.1.0" apply false
}

subprojects {
    group = "net.astopia.core"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "org.jetbrains.kotlin.jvm")
}
