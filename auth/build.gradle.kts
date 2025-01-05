import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(path = ":app"))
}

tasks {
    named<BootJar>("bootJar") {
        enabled = false
    }
}

