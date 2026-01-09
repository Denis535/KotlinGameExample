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

include("project")
include("project.ui")
include("project.app")
include("project.game")
include("project.common")

project(":project").projectDir = File("project")
project(":project.ui").projectDir = File("project.10.ui")
project(":project.app").projectDir = File("project.20.app")
project(":project.game").projectDir = File("project.50.game")
project(":project.common").projectDir = File("project.60.common")
