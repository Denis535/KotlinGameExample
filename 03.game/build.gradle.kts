plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC3"
}

kotlin {
    this.mingwX64()
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation(this.project(":common"))
            }
        }
        val mingwX64Main by getting {
        }
    }
}
