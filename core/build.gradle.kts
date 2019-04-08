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
        buildConfigField("String", "API_KEY", "${rootProject.ext["API_KEY"]}")
        buildConfigField("String", "SHARED_SECRET", "${rootProject.ext["SHARED_SECRET"]}")
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
    implementation(Deps.ktorClientAndroid)
    implementation(Deps.ktorClientJsonJvm)
    implementation(Deps.ktorClientLoggingJvm)
    kapt(Deps.glideCompiler)

    testImplementation(Deps.junit)
    testImplementation(Deps.guava)
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.3.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.3.1")

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.3.1")

    testImplementation("androidx.test:core:1.0.0")
    testImplementation(Deps.spek)
    testImplementation(Deps.spekJunitPlatformEngine)
    testImplementation(Deps.kotlinReflect)
    testImplementation("org.junit.platform:junit-platform-runner:1.1.0")
    testImplementation(Deps.kotlinTestJunit)
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