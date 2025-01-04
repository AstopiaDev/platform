import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation("com.sparkjava:spark-core:2.9.4")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("Votifier-Megamind.jar")
    }
}
