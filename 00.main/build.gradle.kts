plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64 {
        this.binaries {
            this.executable {
                this.baseName = "Launcher"
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts("-Wl,-subsystem,windows")
            }
        }
    }
    this.linuxX64 {
        this.binaries {
            this.executable {
                this.baseName = "Launcher"
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
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}

//tasks.register<Copy>("install-debug-dependencies-mingw") {
//    this.from("../sdl/SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
//    this.into(kotlin.mingwX64().binaries.getExecutable("DEBUG").outputDirectory)
//}
//tasks.register<Copy>("install-release-dependencies-mingw") {
//    this.from("../sdl/SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
//    this.into(kotlin.mingwX64().binaries.getExecutable("RELEASE").outputDirectory)
//}
//tasks.register<Copy>("install-debug-dependencies-linux") {
//    this.from("../sdl/SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
//    this.into(kotlin.linuxX64().binaries.getExecutable("DEBUG").outputDirectory)
//}
//tasks.register<Copy>("install-release-dependencies-linux") {
//    this.from("../sdl/SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
//    this.into(kotlin.linuxX64().binaries.getExecutable("RELEASE").outputDirectory)
//}

//tasks.named("linkDebugExecutableMingwX64") {
//    this.finalizedBy("install-debug-dependencies-mingw")
//}
//tasks.named("linkReleaseExecutableMingwX64") {
//    this.finalizedBy("install-release-dependencies-mingw")
//}
//tasks.named("linkDebugExecutableLinuxX64") {
//    this.finalizedBy("install-debug-dependencies-linux")
//}
//tasks.named("linkReleaseExecutableLinuxX64") {
//    this.finalizedBy("install-release-dependencies-linux")
//}

val OperationSystem = System.getProperty("os.name")!!

tasks.register<Exec>("run") {
    val target = when {
        OperationSystem.lowercase().contains("windows") -> kotlin.mingwX64()
        OperationSystem.lowercase().contains("linux") -> kotlin.linuxX64()
        else -> error("Operation system $OperationSystem is unsupported")
    }
    val executable = target.binaries.getExecutable("DEBUG")
    this.dependsOn(executable.linkTaskProvider)
    this.commandLine(executable.outputFile.absolutePath)
}

tasks.register("publish") {
    this.dependsOn(tasks.named("clean-dist"), tasks.named("publish-to-dist-windows"), tasks.named("publish-to-dist-linux"))
}

tasks.register<Delete>("clean-dist") {
    this.delete(layout.projectDirectory.dir("dist"))
}

tasks.register<Copy>("publish-to-dist-windows") {
    val target = kotlin.mingwX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../sdl/SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
    this.into(layout.projectDirectory.dir("dist/windows"))
}

tasks.register<Copy>("publish-to-dist-linux") {
    val target = kotlin.linuxX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../sdl/SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
    this.into(layout.projectDirectory.dir("dist/linux"))
}
