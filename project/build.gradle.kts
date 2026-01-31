plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64 {
        this.binaries {
            this.executable {
                this.baseName = "KotlinGameExample"
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts(
//                    "-Wl,--verbose",
                    "-Wl,-subsystem,windows",
                )
            }
        }
    }
    this.linuxX64 {
        this.binaries {
            this.executable {
                this.baseName = "KotlinGameExample"
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts(
                    "-Lcontent/libs/x86_64-linux-gnu/SDL/lib",
                    "-lSDL3",
//                    "-Wl,--verbose",
                    "-Wl,--allow-shlib-undefined",
                    "-Wl,-rpath,\$ORIGIN",
                )
            }
        }
    }
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
            this.dependencies {
                this.implementation(this.project(":project.ui"))
                this.implementation(this.project(":project.app"))
                this.implementation(this.project(":project.game"))
                this.implementation(this.project(":project.common"))
            }
        }
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}

val OperationSystem = System.getProperty("os.name")!!
if (OperationSystem.lowercase().contains("windows")) {
    tasks.register<Exec>("run") {
        val executable = kotlin.mingwX64().binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        this.environment(
            "PATH", listOfNotNull(
                "../content/libs/x86_64-w64-mingw32/SDL/lib", System.getenv("PATH")
            ).joinToString(";")
        )
        this.commandLine(executable.outputFile)
    }
} else if (OperationSystem.lowercase().contains("linux")) {
    tasks.register<Exec>("run") {
        val executable = kotlin.linuxX64().binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        this.environment(
            "LD_LIBRARY_PATH", listOfNotNull(
                "../content/libs/x86_64-linux-gnu/SDL/lib", System.getenv("LD_LIBRARY_PATH")
            ).joinToString(":")
        )
        this.commandLine(executable.outputFile)
    }
}

tasks.register("publish") {
    this.dependsOn(tasks.named("publish-x86_64-w64-mingw32"))
    this.dependsOn(tasks.named("publish-x86_64-linux-gnu"))
}

tasks.register<Copy>("publish-x86_64-w64-mingw32") {
    val executable = kotlin.mingwX64().binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/Icon.png")
    this.from("../content/Licenses") { this.into("Licenses") }
    this.from("../content/libs/x86_64-w64-mingw32/SDL/bin/SDL3.dll")
    this.from("../content-bundle") { this.into("Content") }
    this.into(layout.projectDirectory.dir("dist/Windows-x86_64"))
}

tasks.register<Copy>("publish-x86_64-linux-gnu") {
    val executable = kotlin.linuxX64().binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/Icon.png")
    this.from("../content/Licenses") { this.into("Licenses") }
    this.from("../content/libs/x86_64-linux-gnu/SDL/lib/libSDL3.so.0")
    this.from("../content/libs/x86_64-linux-gnu/SDL/lib/libSDL3.so.0.4.0")
    this.from("../content-bundle") { this.into("Content") }
    this.into(layout.projectDirectory.dir("dist/Linux-x86_64"))
}
