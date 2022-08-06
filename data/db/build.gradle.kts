import dependency.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
}

android {
  compileSdk = Versions.compileSdkVersion
  defaultConfig {
    minSdk = Versions.minSdkVersion
    targetSdk = Versions.targetSdkVersion
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
  implementation(project(":core"))

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.room.runtime)
  kapt(libs.room.compiler)

  implementation(libs.datastore.preferences)
}
