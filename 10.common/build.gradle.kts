plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC2"
}

kotlin {
    this.mingwX64 {
        this.compilations.getByName("main") {
            this.cinterops {
                val glfw by creating {
                    this.definitionFile = file("native/glfw.def")
                }
            }
        }
    }
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.api("io.github.denis535:game-framework-pro:1.0.1")
                this.api("io.github.denis535:game-framework-pro-extensions:1.0.1")
            }
        }
        val mingwX64Main by getting {
        }
    }
}
