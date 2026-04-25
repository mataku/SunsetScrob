import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.api.dsl.TestExtension

plugins {
  id("com.android.test")
  id("androidx.baselineprofile")
}

configure<TestExtension>() {
  namespace = "com.mataku.scrobscrob.baselineprofile"
  compileSdk = 35

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  
  defaultConfig {
    minSdk = 28
    targetSdk = 35

    testInstrumentationRunnerArguments["androidx.benchmark.suppressErrors"] = "EMULATOR"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  targetProjectPath = ":app"

  testOptions.managedDevices.allDevices {
    create<ManagedVirtualDevice>("pixel9Api35") {
      device = "Pixel 9"
      apiLevel = 35
      systemImageSource = "google"
    }
  }
}

// This is the configuration block for the Baseline Profile plugin.
// You can specify to run the generators on a managed devices or connected devices.
baselineProfile {
  managedDevices += "pixel9Api35"
  useConnectedDevices = false
}

dependencies {
  implementation(libs.androidx.test.ext.junit)
  implementation(libs.androidx.espresso.core)
  implementation(libs.androidx.test.uiautomator)
  implementation(libs.androidx.benchmark.macro.junit4)
}
