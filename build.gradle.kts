plugins {}

//tasks.register("publish") {
//    this.dependsOn(tasks.named("clean-dist"), tasks.named("publish-to-dist"))
//}
//
//tasks.register<Delete>("clean-dist") {
//    this.delete(layout.projectDirectory.dir("dist"))
//}
//
//if (System.getProperty("os.name").lowercase().contains("windows")) {
//    tasks.register<Copy>("publish-to-dist") {
//        val main = project(":main")
//        val linkExecutableTask = main.tasks.named("linkReleaseExecutableMingwX64")
//        val outputDirectory = main.kotlin.mingwX64().binaries.getExecutable("RELEASE").outputDirectory
//        this.dependsOn(linkExecutableTask)
//        this.from(outputDirectory) {
//            this.include("*")
//        }
//        this.into(layout.projectDirectory.dir("dist"))
//    }
//} else if (System.getProperty("os.name").lowercase().contains("linux")) {
//    tasks.register<Copy>("publish-to-dist") {
//        val main = project(":main")
//        val linkExecutableTask = main.tasks.named("linkReleaseExecutableLinuxX64")
//        val outputDirectory = main.kotlin.linuxX64().binaries.getExecutable("RELEASE").outputDirectory
//        this.dependsOn(linkExecutableTask)
//        this.from(outputDirectory) {
//            this.include("*")
//        }
//        this.into(layout.projectDirectory.dir("dist"))
//    }
//}
