plugins {
    id("com.android.dynamic-feature")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("com.cookpad.android.licensetools")
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
        }
        getByName("debug") {
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
    implementation(project(":app"))
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
