plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.test_helper.integration"
  }

  compilerOptions {
    freeCompilerArgs.add("-opt-in=androidx.compose.ui.test.ExperimentalTestApi")
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":core"))
      implementation(project(":ui_common"))
      implementation(libs.jetbrains.compose.ui.test)
      implementation(libs.kotlinx.collections.immutable)
      implementation(libs.roborazzi.core)
    }
    androidMain.dependencies {
      implementation(libs.robolectric)
      implementation(libs.roborazzi)
      implementation(libs.androidx.test.ext.junit)
      implementation(libs.compose.ui.test.junit4)
      implementation(libs.compose.ui.test.manifest)
    }
    jvmMain.dependencies {
      api(compose.desktop.currentOs)
      api(libs.roborazzi.compose.desktop)
      api(libs.coroutines.swing)
    }
  }
}
