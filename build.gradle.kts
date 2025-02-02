plugins {
    kotlin("jvm") version "2.0.21"
    id("application") // ❗ `application` ni to‘g‘ri qo‘shish
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    // Telegram bot kutubxonasi
    implementation("io.github.kotlin-telegram-bot.kotlin-telegram-bot:telegram:6.2.0")
}

application {  // ✅ To‘g‘ri ishlatish
    mainClass.set("org.example.MainKt")

}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}