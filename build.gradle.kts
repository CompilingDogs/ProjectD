plugins {
    java
    kotlin("jvm") version "1.4.0"
    kotlin("kapt") version "1.4.0"
    application
}

application {
    mainClassName = "Main"
}

group = "com.compilingdogs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven(url = "https://dl.bintray.com/arrow-kt/arrow-kt/")
}


dependencies {
    implementation(kotlin("stdlib", "1.4.0"))
    implementation("junit", "junit", "4.12")
    implementation("com.google.code.gson", "gson", "2.8.6")
    implementation("org.apache.logging.log4j", "log4j-slf4j-impl", "2.13.3")
}
