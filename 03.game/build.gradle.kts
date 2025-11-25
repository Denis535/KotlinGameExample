plugins {
    this.id("org.jetbrains.kotlin.jvm") version "2.2.21"
    this.id("maven-publish")
}

group = project.group
version = project.version
description = project.description

kotlin {
    this.jvmToolchain(21)
    this.compilerOptions {
        this.jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

dependencies {
    this.implementation(this.project(":common"))
}

publishing {
    this.publications {
        this.create<MavenPublication>("mavenJava") {
            this.from(components["java"])
            this.groupId = project.group.toString()
            this.artifactId = rootProject.name + '-' + project.name
            this.version = project.version.toString()
            this.pom {
                this.name = project.name
                this.description = project.description
                this.url = project.findProperty("url").toString()
                this.licenses {
                    this.license {
                        this.name = "MIT License"
                        this.url = "https://opensource.org/licenses/MIT"
                    }
                }
                this.developers {
                    this.developer {
                        this.id = "denis535"
                        this.name = "Denis535"
                    }
                }
                this.scm {
                    this.connection = "scm:git:git://github.com/Denis535/kotlin-game-example.git"
                    this.developerConnection = "scm:git:ssh://git@github.com:Denis535/kotlin-game-example.git"
                    this.url = "https://github.com/Denis535/kotlin-game-example"
                }
            }
        }
    }
    this.repositories {
        this.maven {
            this.name = "Local"
            this.url = uri("distribution")
        }
    }
}
