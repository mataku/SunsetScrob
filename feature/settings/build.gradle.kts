plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}

apply {
    from("$rootDir/core_dependencies.gradle")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)

    dataBinding.isEnabled = true

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    lintOptions {
        isAbortOnError = false
        textReport = true
        textOutput("stdout")
        xmlReport = false
    }
}

dependencies {
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven("https://maven.google.com")
}

kapt {
    correctErrorTypes = true
}
