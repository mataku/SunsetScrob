import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
  id("com.android.test")
  id("org.jetbrains.kotlin.android")
  id("androidx.baselineprofile")
}

android {
  namespace = "com.mataku.scrobscrob.baselineprofile"
  compileSdk = 35

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = "17"
  }

  defaultConfig {
    minSdk = 28
    targetSdk = 35

    testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  targetProjectPath = ":app"

  testOptions.managedDevices.allDevices {
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
  useConnectedDevices = false
}

dependencies {
  implementation(libs.androidx.test.ext.junit)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro.junit4)
}
