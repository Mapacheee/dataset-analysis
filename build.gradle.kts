plugins {
    id("java")
    id("com.gradleup.shadow") version "9.2.2"
    id("application")
}

group = "org.group"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv:opencsv:5.12.0")
}
tasks.test {}

application {
    mainClass.set("org.group.analysis.ProyectoAnalisis")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.group.analysis.ProyectoAnalisis"
    }
}

tasks {
    shadowJar {
        archiveFileName.set("tarea-${version}.jar")
    }
}