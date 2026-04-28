plugins {
    id("java")
    id("application")
}

group = "org.group"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {}
tasks.test {}

application {
    mainClass.set("org.group.analysis.AnalysisProject")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.group.analysis.AnalysisProject"
    }
}
