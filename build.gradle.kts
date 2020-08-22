plugins {
    java
    kotlin("jvm") version "1.3.72"
    id("io.freefair.lombok") version "5.1.1"
}

group = "com.compilingdogs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    testCompile("junit", "junit", "4.12")
    compile("org.apache.logging.log4j", "log4j-slf4j-impl", "2.13.3")
}
