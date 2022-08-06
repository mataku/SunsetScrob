import dependency.Versions

plugins {
  id("com.android.library")
  id("org.jetbrains.kotlin.android")
}

android {
  compileSdk = Versions.compileSdkVersion

  defaultConfig {
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  implementation(libs.androidx.test.core)
  implementation(libs.kotest.runner.junit5)
  implementation(libs.kotest.assertions)
  implementation(libs.mockk)
  implementation(libs.mockk.agent.jvm)
  implementation(libs.coroutines.test)
  implementation(libs.coroutines)
}