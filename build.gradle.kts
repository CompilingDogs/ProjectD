import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.4.0"
    application
}

application {
    mainClassName = "Main"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

group = "com.compilingdogs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib", "1.4.0"))
    implementation("junit", "junit", "4.12")
    implementation("com.google.code.gson", "gson", "2.8.6")
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", "2.13.3")
}
