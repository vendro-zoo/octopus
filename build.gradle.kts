import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.0"
    id("maven-publish")  // Used to publish to the local maven repository
    id("org.jetbrains.dokka") version "1.6.21"  // Used to generate the API documentation
}

group = "it.vendro-zoo"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.3")
    testImplementation(kotlin("test"))

    dokkaJavadocPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.21")  // Used to generate the API documentation as javadoc
}

// Function to build source jar file
val sourcesJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn(JavaPlugin.CLASSES_TASK_NAME)
    archiveClassifier.set("sources")
    from(sourceSets["main"].allSource)
}

// Function to build javadoc jar file
val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    dependsOn("dokkaJavadoc")
    archiveClassifier.set("javadoc")
    from(tasks.javadoc)
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Publish all the jar files to the local maven repository
publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "it.zoo.vendro"
            artifactId = "octopus"
            version = version

            artifact(tasks.jar)
            artifact(sourcesJar)
            artifact(javadocJar)
        }
    }
}