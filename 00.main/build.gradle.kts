plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
//    this.mingwX64 {
//        this.binaries {
//            this.executable {
//                this.baseName = rootProject.name
//                this.entryPoint = "com.denis535.kotlin_game_example.Main"
//                this.linkerOpts("-Wl,-subsystem,windows")
//            }
//        }
//    }
    this.linuxX64 {
        this.binaries {
            this.executable {
                this.baseName = rootProject.name
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts("-Wl,--allow-shlib-undefined")
            }
        }
    }
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation(this.project(":ui"))
                this.implementation(this.project(":app"))
                this.implementation(this.project(":game"))
                this.implementation(this.project(":common"))
            }
        }
//        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}
