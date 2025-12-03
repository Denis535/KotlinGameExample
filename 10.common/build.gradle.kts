plugins {
    this.id("org.jetbrains.kotlin.jvm") version "2.2.21"
    this.id("com.github.ben-manes.versions") version "0.53.0"
}

kotlin {
    this.jvmToolchain(24)
    this.compilerOptions {
        this.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24
    }
}

dependencies {
    this.api("io.github.denis535:game-framework-pro:1.0.1")
    this.api("io.github.denis535:game-framework-pro-extensions:1.0.1")
    this.implementation(this.platform("org.lwjgl:lwjgl-bom:3.3.6"))
    this.implementation("org.lwjgl", "lwjgl")
    this.implementation("org.lwjgl", "lwjgl-glfw")
    this.implementation("org.lwjgl", "lwjgl", classifier = "natives-windows")
    this.implementation("org.lwjgl", "lwjgl-glfw", classifier = "natives-windows")
}

tasks.jar {
    this.archiveBaseName = rootProject.name + '-' + project.name
    this.archiveVersion = ""
    this.archiveClassifier = ""
}
