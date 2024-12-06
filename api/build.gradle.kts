import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.openapi.generator") version "7.5.0"
}

group = "org.bbakaple"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_17

repositories.mavenCentral()

dependencies {
    implementation(project(path = ":app"))
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0") // Swagger
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.15.2")) // 원하는 Jackson 버전으로 변경
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val openApiPackages = Pair(
        "openapi",
        listOf("openapi.api", "openapi.invoker", "openapi.model"),
)

val openApiGeneratedDir = "openApiGenerate"
val contractDir = "contract"
val dirs = mapOf(
        contractDir to "$rootDir/api/docs/contract",
        openApiGeneratedDir to "$buildDir/openapi",
)

val contractFileNames = fileTree(dirs[contractDir]!!)
        .filter { it.extension == "yaml" }
        .map { it.name }

val generateOpenApiTasks = contractFileNames.map { createOpenApiGenerateTask(it) }

tasks.register("createOpenApi") {
    doFirst {
        println("Creating Code By OpenAPI... $rootDir")
    }
    doLast {
        println("OpenAPI Code created.")
    }
    dependsOn(generateOpenApiTasks)
}

tasks.register("moveGeneratedSources") {
    doFirst {
        println("Moving generated sources...")
    }
    doLast {
        openApiPackages.second
                .map { it.replace(".", "/") }
                .forEach { packagePath ->
                    val originDir = file("${dirs[openApiGeneratedDir]}/src/main/kotlin/$packagePath")
                    val destinationDir = file("$buildDir/generated/$packagePath")
                    originDir.listFiles { file -> file.extension == "kt" }?.forEach { file ->
                        val resolvedFile = destinationDir.resolve(file.name)
                        if (!resolvedFile.exists() && file.name != "Application.kt") {
                            file.copyTo(destinationDir.resolve(file.name), true)
                        }
                    }
                }
        println("Generated sources moved.")
    }
    dependsOn("createOpenApi")
}

tasks.register("cleanGeneratedDirectory") {
    doFirst {
        println("Cleaning generated directory...")
    }
    doLast {
        file(dirs[openApiGeneratedDir]!!).deleteRecursively()
        println("Generated directory cleaned.")
    }
    dependsOn("moveGeneratedSources")
}

sourceSets {
    main {
        kotlin.srcDir("$buildDir/generated")
    }
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
    dependsOn("cleanGeneratedDirectory")
}

fun createOpenApiGenerateTask(fileName: String) = tasks.register<GenerateTask>("openApiGenerate_$fileName") {
    generatorName.set("kotlin-spring")
    inputSpec.set("${dirs[contractDir]}/$fileName")
    outputDir.set(dirs[openApiGeneratedDir])
    apiPackage.set(openApiPackages.second[0])
    invokerPackage.set(openApiPackages.second[1])
    modelPackage.set(openApiPackages.second[2])
    configOptions.set(
            mapOf(
                    "dateLibrary" to "kotlin-spring",
                    "useSpringBoot3" to "true",
                    "useTags" to "true",
                    "interfaceOnly" to "true",
            ),
    )
    templateDir.set("${dirs[contractDir]}/template")
}
