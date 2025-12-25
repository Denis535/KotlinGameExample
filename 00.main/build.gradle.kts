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
//                this.linkerOpts("-Wl,--allow-shlib-undefined")
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

val OperationSystem = System.getProperty("os.name")!!
if (OperationSystem.lowercase().contains("windows")) {
    tasks.register<Exec>("run") {
        val target = kotlin.mingwX64()
        val executable = target.binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        val paths = listOfNotNull(
            "../sdl/SDL-release-3.2.28/install/x86_64-w64-mingw32/bin", System.getenv("PATH")
        )
        this.environment("PATH", paths.joinToString(";"))
        this.commandLine(executable.outputFile)
    }
} else if (OperationSystem.lowercase().contains("linux")) {
    tasks.register<Exec>("run") {
        val target = kotlin.linuxX64()
        val executable = target.binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        val paths = listOfNotNull(
            "../sdl/SDL-release-3.2.28/install/x86_64-linux-gnu/lib", System.getenv("LD_LIBRARY_PATH")
        )
        this.environment("LD_LIBRARY_PATH", paths.joinToString(":"))
        this.commandLine(executable.outputFile)
    }
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
