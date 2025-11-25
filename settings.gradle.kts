pluginManagement {
    this.repositories {
        this.gradlePluginPortal()
        this.mavenCentral()
    }
}

dependencyResolutionManagement {
    this.repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    this.repositories {
        this.mavenCentral()
    }
}

plugins {
    this.id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "kotlin-game-example"

include("00.main")
include("01.ui")
include("02.app")
include("03.game")
include("10.common")
