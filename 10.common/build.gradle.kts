plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC2"
    this.id("com.github.ben-manes.versions") version "0.53.0"
}

kotlin {
    this.mingwX64("windows")
    this.sourceSets {
        val main by this.creating {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.api("io.github.denis535:game-framework-pro:1.0.1")
                this.api("io.github.denis535:game-framework-pro-extensions:1.0.1")
            }
        }
        val windowsMain by getting {
            this.dependsOn(main)
        }
    }
}
