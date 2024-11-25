import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("com.android.test")
  id("org.jetbrains.kotlin.android")
  id("androidx.baselineprofile")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.mataku.scrobscrob.baselineprofile"
  compileSdk = 34

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  buildFeatures {
    buildConfig = true
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  defaultConfig {
    minSdk = 30
    targetSdk = 34

    testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    testInstrumentationRunner = "com.mataku.scrobscrob.baselineprofile.HiltBenchmarkRunner"

    testInstrumentationRunnerArguments["androidx.benchmark.fullTracing.enable"] = "true"

    val properties = gradleLocalProperties(rootDir, providers)
    val username = properties.getProperty("USERNAME")
    val password = properties.getProperty("PASSWORD")
    buildConfigField("String", "USERNAME", username)
    buildConfigField("String", "PASSWORD", password)
  }

  targetProjectPath = ":app"

  testOptions.managedDevices.devices {
    create<ManagedVirtualDevice>("pixel6Api34") {
      device = "Pixel 6"
      apiLevel = 34
      systemImageSource = "google"
    }
  }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
  managedDevices += "pixel6Api34"
  useConnectedDevices = true
}

dependencies {
  implementation(libs.hilt.android)
  ksp(libs.hilt.compiler)
  implementation(libs.hilt.android.testing)
  implementation(libs.androidx.test.ext.junit)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro.junit4)

  implementation(libs.androidx.tracing.perfetto)
  implementation(libs.androidx.tracing.perfetto.binary)
  implementation(libs.androidx.benchmark.junit4)

  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.kotlinx.collection)

  implementation(libs.compose.ui.test.junit4)
  implementation(libs.compose.ui.test.manifest)
  implementation(libs.compose.ui.test.android)
//  implementation(libs.robolectric)
}
