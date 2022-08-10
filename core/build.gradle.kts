import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import dependency.Versions

plugins {
  id("com.android.library")
  id("kotlin-android")
  id("kotlin-kapt")
  id("kotlinx-serialization")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")
apply(from = "${project.rootDir}/gradle/test_options.gradle")

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

    kotlinOptions {
      freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
  }
}

dependencies {
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.logging)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)
  implementation(libs.serialization.json)
  implementation(libs.coroutines)
}

repositories {
  google()
  mavenCentral()
  maven("https://maven.google.com")
  maven("https://kotlin.bintray.com/kotlinx")
}

kapt {
  correctErrorTypes = true

  javacOptions {
    option("-Xmaxerrs", 1000)
  }
}
