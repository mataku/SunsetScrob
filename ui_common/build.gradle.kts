plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlin-android")
  id("kotlin-kapt")
}

android {
  namespace = "com.mataku.scrobscrob.ui_common"
}

dependencies {
  implementation(project(":core"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.accompanist.navigation.animation)
  implementation(libs.accompanist.systemuicontroller)

  implementation(libs.coil.compose)
  implementation(libs.compose.material.icons.extended)
}
