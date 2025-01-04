// TODO: make this beautiful

dependencies {
    api("com.zaxxer:HikariCP:5.0.1")
    api("mysql:mysql-connector-java:8.0.33")

    api("org.apache.httpcomponents:httpclient:4.5.14")

    // Google commons
    api("com.google.guava:guava:33.2.1-jre")

    // TODO: migrate completely to FastUtil
    implementation("it.unimi.dsi:fastutil:8.1.0")

    // YAML and JSON controller
    api("org.yaml:snakeyaml:2.0")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("at.favre.lib:bcrypt:0.10.2")

    // Bytecode library
    api("org.ow2.asm:asm:9.4")

    api(project(":backend:backend-api"))
    api(project(":backend:impl-loader"))
    implementation(project(":backend:protocol"))
    annotationProcessor(project(":backend:impl-loader"))
}

repositories {
    mavenCentral()
}
