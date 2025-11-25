plugins {
}

tasks.register<Copy>("dist") {
    this.dependsOn(project(":main").tasks.named("jar"))
    this.into(layout.projectDirectory.dir("dist"))
    this.from(project(":main").layout.buildDirectory.dir("libs")) {
        this.include("*.jar")
    }
    this.from(project(":main").configurations.getByName("runtimeClasspath")) {
        this.include("*.jar")
    }
}
