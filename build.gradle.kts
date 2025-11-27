plugins {
    this.id("com.github.ben-manes.versions") version "0.53.0"
}

tasks.register("dist") {
    this.dependsOn(tasks.named("clean-dist"), tasks.named("copy-to-dist"))
    val launcherFilePath = layout.projectDirectory.dir("dist").asFile.resolve("Launcher.bat")
    val launcherContent = """
        @echo off
        cd /d "%~dp0"
        java -jar ${project(":main").tasks.named<Jar>("jar").get().archiveBaseName.get()}.jar
        pause
        """.trimIndent()
    this.doLast {
        launcherFilePath.writeText(launcherContent)
    }
}

tasks.register<Delete>("clean-dist") {
    this.delete(layout.projectDirectory.dir("dist"))
}

tasks.register<Copy>("copy-to-dist") {
    this.dependsOn(project(":main").tasks.named("jar"))
    this.into(layout.projectDirectory.dir("dist"))
    this.from(project(":main").layout.buildDirectory.dir("libs")) {
        this.include("*.jar")
        this.into(".")
    }
    this.from(project(":main").configurations.getByName("runtimeClasspath")) {
        this.include("*.jar")
        this.into(".")
    }
}
