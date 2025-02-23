plugins {
    id("com.google.protobuf") version "0.9.4"
    id("java")
}

kotlin {
    jvmToolchain(23)
}

group = "net.astopia.core.protocol"

dependencies {
    api("io.nats:jnats:2.20.5")

    api("com.google.protobuf:protobuf-javalite:4.28.2")
    implementation("com.google.protobuf:protobuf-java-util:4.28.2")

    implementation("com.google.protobuf:protoc:4.28.2")
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

sourceSets {
    main {
        proto {
            srcDir("../proto")
        }
    }
}