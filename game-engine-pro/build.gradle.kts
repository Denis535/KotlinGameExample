plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64()
    this.linuxX64()
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation("io.github.denis535:sdl:3.4.0.2")
            }
        }
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}
