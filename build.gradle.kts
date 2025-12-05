plugins {
}

tasks.register("publish") {
    this.dependsOn(tasks.named("clean-dist"), tasks.named("publish-to-dist"))
}

tasks.register<Delete>("clean-dist") {
    this.delete(layout.projectDirectory.dir("dist"))
}

tasks.register<Copy>("publish-to-dist") {
    this.dependsOn(project(":main").tasks.named("linkReleaseExecutableMingwX64"))
    this.into(layout.projectDirectory.dir("dist"))
    this.from(project(":main").layout.buildDirectory.dir("bin/mingwX64/releaseExecutable")) {
        this.include("*.exe")
        this.rename { "Launcher.exe" }
    }
}
