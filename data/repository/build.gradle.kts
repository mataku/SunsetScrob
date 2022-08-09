import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
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
  implementation(project(":core"))
  implementation(project(":data:api"))
  implementation(project(":data:db"))

  // TODO: Endpoint の requestType を独自の enum にして ktor に依存しないようにした上で消す
  implementation(libs.ktor.client.core)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.timber)
}
