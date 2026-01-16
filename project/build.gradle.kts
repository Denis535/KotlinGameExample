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
                    "-Wl,-subsystem,windows"
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
//                    "-Wl,--verbose",
                    "-Wl,--allow-shlib-undefined",
                    "-Wl,-rpath,\$ORIGIN",
                    "-Llibs/SDL/x86_64-linux-gnu/lib",
                    "-lSDL3",
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
        val target = kotlin.mingwX64()
        val executable = target.binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        this.environment(
            "PATH", listOfNotNull(
                "../libs/SDL/x86_64-w64-mingw32/lib", System.getenv("PATH")
            ).joinToString(";")
        )
        this.commandLine(executable.outputFile)
    }
} else if (OperationSystem.lowercase().contains("linux")) {
    tasks.register<Exec>("run") {
        val target = kotlin.linuxX64()
        val executable = target.binaries.getExecutable("DEBUG")
        this.dependsOn(executable.linkTaskProvider)
        this.environment(
            "LD_LIBRARY_PATH", listOfNotNull(
                "../libs/SDL/x86_64-linux-gnu/lib", System.getenv("LD_LIBRARY_PATH")
            ).joinToString(":")
        )
        this.commandLine(executable.outputFile)
    }
}

tasks.register("publish") {
    this.dependsOn(tasks.named("publish-build-x86_64-w64-mingw32"))
    this.dependsOn(tasks.named("publish-build-x86_64-linux-gnu"))
    this.dependsOn(tasks.named("publish-content"))
}

tasks.register<Copy>("publish-build-x86_64-w64-mingw32") {
    val target = kotlin.mingwX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/Icon.png")
    this.from("../libs/SDL/x86_64-w64-mingw32/bin")
    this.from("../libs/SDL/x86_64-w64-mingw32/share")
    this.into(layout.projectDirectory.dir("dist/Windows-x86_64"))
}

tasks.register<Copy>("publish-build-x86_64-linux-gnu") {
    val target = kotlin.linuxX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/Icon.png")
    this.from("../libs/SDL/x86_64-linux-gnu/lib/libSDL3.so.0")
    this.from("../libs/SDL/x86_64-linux-gnu/lib/libSDL3.so.0.4.0")
    this.from("../libs/SDL/x86_64-linux-gnu/share")
    this.into(layout.projectDirectory.dir("dist/Linux-x86_64"))
}

tasks.register<Tar>("publish-content") {
    this.destinationDirectory = layout.projectDirectory.dir("dist")
    this.archiveBaseName = "Content"
    this.archiveVersion = ""
    this.compression = Compression.BZIP2
    this.from("../content/content")
}
