plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
    id("realm-android")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)

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
    implementation(Deps.moshi)
    implementation(Deps.moshiKotlin)
    implementation(Deps.glide)
    kapt(Deps.glideCompiler)

    testImplementation(Deps.junit)
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