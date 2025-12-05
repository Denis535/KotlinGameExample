plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0-RC2"
    this.id("com.github.ben-manes.versions") version "0.53.0"
}

kotlin {
    this.mingwX64("windows") {
        this.binaries {
            this.executable {
                this.baseName = rootProject.name
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
            }
        }
    }
    this.sourceSets {
        val main by this.creating {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation(this.project(":ui"))
                this.implementation(this.project(":app"))
                this.implementation(this.project(":game"))
                this.implementation(this.project(":common"))
            }
        }
        val windowsMain by getting {
            this.dependsOn(main)
        }
    }
}

//tasks.register("run") {
//    this.dependsOn("nativeDebugExecutable")
//    this.doLast {
//        val exe = kotlin.targets["native"].binaries.getExecutable("DEBUG").outputFile
//        exec {
//            this.commandLine(exe.absolutePath)
//        }
//    }
//}
