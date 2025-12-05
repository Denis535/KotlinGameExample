plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC2"
}

kotlin {
    this.mingwX64()
    this.sourceSets {
        val main by this.creating {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation(this.project(":game"))
                this.implementation(this.project(":common"))
            }
        }
        val mingwX64Main by getting {
            this.dependsOn(main)
        }
    }
}
