import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

dependencies {
    implementation("net.dv8tion:JDA:5.0.0-beta.20")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    withType<ShadowJar> {
        archiveFileName.set("Discord-Megamind.jar")
    }
}
