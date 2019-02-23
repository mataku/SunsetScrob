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

}

dependencies {
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
