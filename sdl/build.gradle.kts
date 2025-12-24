plugins {
    this.id("org.jetbrains.kotlin.multiplatform") version "2.3.0"
}

kotlin {
    this.mingwX64 {
        this.compilations["main"].cinterops {
            val sdl by creating {
                this.definitionFile = file("com.denis535.sdl.def")
            }
        }
    }
    this.linuxX64 {
        this.compilations["main"].cinterops {
            val sdl by creating {
                this.definitionFile = file("com.denis535.sdl.def")
            }
        }
    }
    this.sourceSets {
        val commonMain by this.getting {
            this.kotlin.srcDir("sources")
            this.resources.srcDir("resources")
        }
        val mingwX64Main by getting {}
        val linuxX64Main by getting {}
    }
}

//tasks.register<Copy>("copy-mingw-debug-dependencies") {
//    this.from("SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
//    this.into(layout.buildDirectory.dir("bin/${kotlin.mingwX64().binaries.getExecutable("DEBUG").outputFile.name}"))
//}
//
//tasks.register<Copy>("copy-mingw-release-dependencies") {
//    this.from("SDL-release-3.2.28/install/x86_64-w64-mingw32/bin/SDL3.dll")
//    this.into(layout.buildDirectory.dir("bin/${kotlin.mingwX64().binaries.getExecutable("RELEASE").outputFile.name}"))
//}
//
//tasks.register<Copy>("copy-linux-debug-dependencies") {
//    this.from("SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
//    this.into(layout.buildDirectory.dir("bin/${kotlin.linuxX64().binaries.getExecutable("DEBUG").outputFile.name}"))
//}
//
//tasks.register<Copy>("copy-linux-release-dependencies") {
//    this.from("SDL-release-3.2.28/install/x86_64-linux-gnu/lib/libSDL3.so")
//    this.into(layout.buildDirectory.dir("bin/${kotlin.linuxX64().binaries.getExecutable("RELEASE").outputFile.name}"))
//}
//
//tasks.named("linkDebugExecutableMingwX64") {
//    this.finalizedBy("copy-mingw-debug-dependencies")
//}
//
//tasks.named("linkReleaseExecutableMingwX64") {
//    this.finalizedBy("copy-mingw-release-dependencies")
//}
//
//tasks.named("linkDebugExecutableLinuxX64") {
//    this.finalizedBy("copy-linux-debug-dependencies")
//}
//
//tasks.named("linkReleaseExecutableLinuxX64") {
//    this.finalizedBy("copy-linux-release-dependencies")
//}
