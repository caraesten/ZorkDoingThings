plugins {
    kotlin("jvm") version "1.3.72"
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    // TODO: Replace versions with imported properties
    implementation("org.twitter4j:twitter4j-core:4.0.7")
    implementation("io.ktor:ktor-server-core:1.3.2")
    implementation("io.ktor:ktor-server-netty:1.3.2")
    implementation("io.ktor:ktor-mustache:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}

application {
    mainClassName = "io.ktor.server.netty.EngineMain"
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}