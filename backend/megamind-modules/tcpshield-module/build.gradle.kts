import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation("com.google.code.gson:gson:2.10.1") // https://mvnrepository.com/artifact/com.squareup.okhttp/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.0")

    implementation(project(":backend:protocol"))
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("TcpShield-Megamind.jar")
    }
}
