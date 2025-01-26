dependencies {
    compileOnly("net.astopia:velocity-native:3.3.2")
    //compileOnly("net.astopia:authmevelocity:4.1.1")

    annotationProcessor("net.astopia:velocity-native:3.3.2")
    annotationProcessor(project(":backend:impl-loader"))

    api(project(":backend:base")) {
        exclude(group = "com.google.guava", module = "guava")
        exclude(group = "org.yaml", module = "snakeyaml")

        exclude(group = "io.netty", module = "netty-handler")
        exclude(group = "io.netty", module = "netty-transport-native-epoll")
        exclude(group = "io.netty", module = "netty-codec")
        exclude(group = "com.maxmind.geoip2", module = "geoip2")
        exclude(group = "javatar", module = "javatar")
    }

    api(project(":backend:protocol")) {
        exclude(group = "io.netty", module = "netty-handler")
        exclude(group = "io.netty", module = "netty-transport-native-epoll")
        exclude(group = "io.netty", module = "netty-codec")
    }
}

tasks {
    shadowJar {
        val output = project(":backend").ext["output"] as String
        destinationDirectory.set(file("$rootDir/$output"))
        archiveFileName.set("VelocityConnector.jar")
    }
}
