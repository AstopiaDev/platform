plugins {
    id("com.google.protobuf") version "0.9.4"
    id("java")
}

group = "net.astopia.core.megamind"

dependencies {
    implementation(project(":core:protocol"))
    implementation(project(":core:minimind"))

    implementation("com.google.protobuf:protobuf-java-util:4.28.2")

    implementation("com.google.protobuf:protoc:4.28.2")
}

kotlin {
    jvmToolchain(23)
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.28.2"
    }
    generateProtoTasks {
        all().configureEach {
            builtins {
                named("java") {
                    option("lite")
                }
            }
        }
    }
}

