plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64 {
        this.binaries {
            this.executable {
                this.baseName = "Launcher"
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts(
                    "-Wl,-subsystem,windows"
                )
            }
        }
    }
    this.linuxX64 {
        this.binaries {
            this.executable {
                this.baseName = "Launcher"
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts(
                    "-Wl,--allow-shlib-undefined",
                    "-Wl,-rpath,\$ORIGIN",
                    "-Lcontent/x86_64-linux-gnu",
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
        this.environment(
            "PATH", listOfNotNull(
                "../content/00.main/x86_64-w64-mingw32", System.getenv("PATH")
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
                "../content/00.main/x86_64-linux-gnu", System.getenv("LD_LIBRARY_PATH")
            ).joinToString(":")
        )
        this.commandLine(executable.outputFile)
    }
}

tasks.register("publish") {
    this.dependsOn(tasks.named("publish-clean"), tasks.named("publish-x86_64-w64-mingw32"), tasks.named("publish-x86_64-linux-gnu"))
}

tasks.register<Delete>("publish-clean") {
    this.delete(layout.projectDirectory.dir("dist"))
}

tasks.register<Copy>("publish-x86_64-w64-mingw32") {
    val target = kotlin.mingwX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/00.main/common")
    this.from("../content/00.main/x86_64-w64-mingw32")
    this.from("../content/01.ui") { into("content/ui") }
    this.from("../content/02.app") { into("content/app") }
    this.from("../content/03.game") { into("content/game") }
    this.from("../content/10.common") { into("content/common") }
    this.into(layout.projectDirectory.dir("dist/Windows-x86_64"))
}

tasks.register<Copy>("publish-x86_64-linux-gnu") {
    val target = kotlin.linuxX64()
    val executable = target.binaries.getExecutable("RELEASE")
    this.dependsOn(executable.linkTaskProvider)
    this.from(executable.outputDirectory)
    this.from("../content/00.main/common")
    this.from("../content/00.main/x86_64-linux-gnu")
    this.from("../content/01.ui") { into("content/ui") }
    this.from("../content/02.app") { into("content/app") }
    this.from("../content/03.game") { into("content/game") }
    this.from("../content/10.common") { into("content/common") }
    this.into(layout.projectDirectory.dir("dist/Linux-x86_64"))
}
