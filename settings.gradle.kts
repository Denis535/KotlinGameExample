pluginManagement {
    this.repositories {
        this.gradlePluginPortal()
        this.mavenCentral()
    }
}

dependencyResolutionManagement {
    this.repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    this.repositories {
        this.mavenCentral()
        this.maven {
            this.url = uri("https://maven.pkg.github.com/KotlinOpenFoundation/glfw")
            this.credentials {
                this.username = System.getenv("GITHUB_USERNAME") ?: System.getenv("GITHUB_ACTOR")
                this.password = System.getenv("GITHUB_PASSWORD") ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
}

plugins {
    this.id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "kotlin-game-example"

include("main")
include("ui")
include("app")
include("game")
include("common")

project(":main").projectDir = File("00.main")
project(":ui").projectDir = File("01.ui")
project(":app").projectDir = File("02.app")
project(":game").projectDir = File("03.game")
project(":common").projectDir = File("10.common")
