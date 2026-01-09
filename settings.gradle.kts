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
project(":ui").projectDir = File("10.ui")
project(":app").projectDir = File("20.app")
project(":game").projectDir = File("50.game")
project(":common").projectDir = File("60.common")
