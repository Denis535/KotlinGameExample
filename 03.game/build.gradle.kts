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
    this.implementation(this.project(":common"))
}

tasks.jar {
    this.archiveBaseName = rootProject.name + '-' + project.name
    this.archiveVersion = ""
    this.archiveClassifier = ""
}
