plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlin-android")
  id("kotlin-kapt")
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
