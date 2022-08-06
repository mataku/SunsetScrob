import dependency.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  compileSdk = Versions.compileSdkVersion
  buildFeatures {
    compose = true
  }

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
  composeOptions {
    kotlinCompilerExtensionVersion = libs.versions.kotlin.compiler.extension.get()
  }

  kotlinOptions {
    jvmTarget = "1.8"
  }
}

dependencies {
  implementation(project(":core"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.navigation)
  implementation(libs.accompanist.navigation.animation)
  implementation(libs.accompanist.systemuicontroller)
}
