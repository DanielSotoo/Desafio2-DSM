plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.google.gms.google.services) apply false
}

buildscript {
    repositories {
        google()        // 🔹 NECESARIO para Firebase y AndroidX
        mavenCentral()
    }
    dependencies {
        classpath(libs.google.services) // 🔹 plugin de Google Services
    }
}
