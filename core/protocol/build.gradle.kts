plugins {
    id("com.google.protobuf") version "0.9.4"
    id("java")
}

group = "me.vihara.core.protocol"

dependencies {
    api("io.netty:netty-handler:4.1.117.Final")
    api("io.netty:netty-transport-native-epoll:4.1.117.Final:linux-x86_64")
    api("io.netty:netty-codec:4.1.117.Final")

    implementation("com.google.protobuf:protobuf-javalite:4.28.2")
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
