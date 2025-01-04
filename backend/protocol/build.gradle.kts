dependencies {
    compileOnly(project(":backend:backend-api"))
    compileOnly(project(":backend:megamind-api"))

    api("it.unimi.dsi:fastutil:8.1.0")

    // @DEPRECATED
    // TODO: migrate to fast util
    api("net.sf.trove4j:trove4j:3.0.3")

    api(project(":backend:protocol-api"))
    api(project(":backend:impl-loader"))
    annotationProcessor(project(":backend:impl-loader"))
}

repositories {
    mavenCentral()
}
