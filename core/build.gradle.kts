plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlinx-serialization")
}

apply {
    from("$rootDir/test_dependencies.gradle")
}

android {
    compileSdkVersion(Versions.compileSdkVersion)

    defaultConfig {
        minSdkVersion(Versions.minSdkVersion)
        targetSdkVersion(Versions.targetSdkVersion)
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "API_KEY", "${rootProject.ext["API_KEY"]}")
        buildConfigField("String", "SHARED_SECRET", "${rootProject.ext["SHARED_SECRET"]}")
        proguardFiles(getDefaultProguardFile("proguard-android.txt"), file("proguard-rules.pro"))
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
        getByName("debug") {
            isMinifyEnabled = true
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
    implementation(Deps.glide)
    implementation(Deps.ktorClientOkhttp)
    implementation(Deps.ktorClientJsonJvm)
    implementation(Deps.ktorClientLoggingJvm)
    implementation(Deps.okhttp)
    implementation(Deps.okhttpLoggingInterceptor)

    kapt(Deps.glideCompiler)
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven("https://maven.google.com")
    maven("https://kotlin.bintray.com/kotlinx")
}

kapt {
    correctErrorTypes = true
}