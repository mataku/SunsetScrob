plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
  id("sunsetscrob.test.screenshot")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.ui_common"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":core"))
      implementation(libs.jetbrains.compose.material3.adaptive)
      implementation(libs.jetbrains.compose.material3.adaptive.layout)
      implementation(libs.jetbrains.compose.material3.adaptive.navigation)
      implementation(libs.jetbrains.compose.material.icons.extended)
      implementation(libs.jetbrains.compose.ui.backhandler)
      implementation(libs.jetbrains.navigation3.ui)
      implementation(libs.navigation3.runtime)
      implementation(libs.jetbrains.lifecycle.viewmodel.compose)
      implementation(libs.metrox.viewmodel.compose)
      implementation(libs.coil.compose)
      implementation(libs.kotlinx.collections.immutable)
      implementation(libs.serialization.json)
    }
    androidMain.dependencies {
      implementation(libs.activity.compose)
    }
  }
}
