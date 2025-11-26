plugins {}

tasks.register("dist") {
    this.dependsOn(tasks.named("clean-dist"), tasks.named("copy-to-dist"))
    this.doLast {
        layout.projectDirectory.dir("dist").asFile.resolve("Launch.bat").writeText(
            """
            @echo off
            cd /d "%~dp0"
            java -jar kotlin-game-example.jar
            pause
            """.trimIndent()
        )
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
