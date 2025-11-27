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
    this.implementation(this.project(":ui"))
    this.implementation(this.project(":app"))
    this.implementation(this.project(":game"))
    this.implementation(this.project(":common"))
}

tasks.jar {
    this.archiveBaseName = rootProject.name
    this.archiveVersion = ""
    this.archiveClassifier = ""
    this.manifest {
        this.attributes["Main-Class"] = "com.denis535.kotlin_game_example.ProgramKt"
        this.attributes["Class-Path"] = configurations.runtimeClasspath.get().filter { it.name.endsWith(".jar") }.joinToString(" ") { it.name }
    }
}

tasks.register("run", JavaExec::class) {
    this.classpath = sourceSets["main"].runtimeClasspath
    this.mainClass = "com.denis535.kotlin_game_example.ProgramKt"
}
