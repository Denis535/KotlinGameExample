plugins {}

if (System.getProperty("os.name").lowercase().contains("windows")) {
    tasks.register<Exec>("run") {
        val main = project(":main")
        this.dependsOn(main.tasks.named("linkDebugExecutableMingwX64"))
        this.commandLine(main.layout.buildDirectory.file("bin/mingwX64/debugExecutable/${rootProject.name}.exe").get())
    }
} else if (System.getProperty("os.name").lowercase().contains("linux")) {
    tasks.register<Exec>("run") {
        val main = project(":main")
        this.dependsOn(main.tasks.named("linkDebugExecutableLinuxX64"))
        this.commandLine(main.layout.buildDirectory.file("bin/linuxX64/debugExecutable/${rootProject.name}.kexe").get())
    }
}

tasks.register("publish") {
    this.dependsOn(tasks.named("clean-dist"), tasks.named("publish-to-dist"))
}

tasks.register<Delete>("clean-dist") {
    this.delete(layout.projectDirectory.dir("dist"))
}

if (System.getProperty("os.name").lowercase().contains("windows")) {
    tasks.register<Copy>("publish-to-dist") {
        val main = project(":main")
        this.dependsOn(main.tasks.named("linkReleaseExecutableMingwX64"))
        this.from(main.layout.buildDirectory.dir("bin/mingwX64/releaseExecutable")) {
            this.include("*.exe")
            this.rename { "Launcher.exe" }
        }
        this.into(layout.projectDirectory.dir("dist"))
    }
} else if (System.getProperty("os.name").lowercase().contains("linux")) {
    tasks.register<Copy>("publish-to-dist") {
        val main = project(":main")
        this.dependsOn(main.tasks.named("linkReleaseExecutableLinuxX64"))
        this.from(main.layout.buildDirectory.dir("bin/linuxX64/releaseExecutable")) {
            include("*.kexe")
            rename { "Launcher" }
        }
        this.into(layout.projectDirectory.dir("dist"))
    }
}
