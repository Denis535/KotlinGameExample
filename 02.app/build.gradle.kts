plugins {
    this.id("org.jetbrains.kotlin.jvm") version "2.2.21"
//    this.id("maven-publish")
}

kotlin {
    this.jvmToolchain(24)
    this.compilerOptions {
        this.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24
    }
}

dependencies {
    this.implementation(this.project(":game"))
    this.implementation(this.project(":common"))
    this.implementation("io.github.denis535:game-framework-pro:1.0.0")
    this.implementation("io.github.denis535:game-framework-pro-extensions:1.0.0")
    this.testImplementation(this.kotlin("test"))
}
