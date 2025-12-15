plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC3"
}

kotlin {
    this.mingwX64 {
        this.compilations["main"].cinterops {
            val glfw by creating {
                this.definitionFile = file("sources/nativeInterop/cinterop/glfw.def")
            }
        }
    }
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.api("io.github.denis535:game-framework-pro:1.1.2")
                this.api("io.github.denis535:game-framework-pro-extensions:1.1.2")
            }
        }
        val mingwX64Main by getting {
        }
    }
}
