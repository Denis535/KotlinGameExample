plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64 {
        this.binaries {
            this.executable {
                this.baseName = rootProject.name
                this.entryPoint = "com.denis535.kotlin_game_example.Main"
                this.linkerOpts("-Wl,-subsystem,windows")
            }
        }
    }
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
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}

tasks.register<Copy>("copy-mingw-debug-dependencies") {
    this.from("SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
    this.into(kotlin.mingwX64().binaries.getExecutable("DEBUG").outputDirectory)
}

tasks.register<Copy>("copy-mingw-release-dependencies") {
    this.from("SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
    this.into(kotlin.mingwX64().binaries.getExecutable("RELEASE").outputDirectory)
}

tasks.register<Copy>("copy-linux-debug-dependencies") {
    this.from("SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
    this.into(kotlin.linuxX64().binaries.getExecutable("DEBUG").outputDirectory)
}

tasks.register<Copy>("copy-linux-release-dependencies") {
    this.from("SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
    this.into(kotlin.linuxX64().binaries.getExecutable("RELEASE").outputDirectory)
}

tasks.named("linkDebugExecutableMingwX64") {
    this.finalizedBy("copy-mingw-debug-dependencies")
}

tasks.named("linkReleaseExecutableMingwX64") {
    this.finalizedBy("copy-mingw-release-dependencies")
}

tasks.named("linkDebugExecutableLinuxX64") {
    this.finalizedBy("copy-linux-debug-dependencies")
}

tasks.named("linkReleaseExecutableLinuxX64") {
    this.finalizedBy("copy-linux-release-dependencies")
}
