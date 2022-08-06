import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import dependency.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("kotlinx-serialization")
}

android {
  compileSdk = Versions.compileSdkVersion
  defaultConfig {
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    val properties = gradleLocalProperties(rootDir)
    val apiKey = properties.getProperty("API_KEY")
    val sharedSecret = properties.getProperty("SHARED_SECRET")
    buildConfigField("String", "API_KEY", apiKey)
    buildConfigField("String", "SHARED_SECRET", sharedSecret)
  }

  buildTypes {
    getByName("debug") {
    }
    release {
    }
  }

  lint {
    abortOnError = false
    textReport = true
    xmlReport = false
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  implementation(libs.coroutines)
  implementation(libs.serialization.json)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)
  implementation(libs.ktor.client.json)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.content.negotiation)
}
