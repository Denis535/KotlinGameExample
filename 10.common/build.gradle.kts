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
                this.api(this.project(":game-engine-pro"))
                this.api("io.github.denis535:game-framework-pro:1.1.2")
                this.api("io.github.denis535:game-framework-pro-extensions:1.1.2")
            }
        }
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}
