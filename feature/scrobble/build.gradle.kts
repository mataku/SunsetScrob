plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.mataku.scrobscrob.scrobble"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.hilt.navigation.compose)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  kapt(libs.hilt.android.compiler)

  implementation(libs.coroutines)

  implementation(libs.lottie.compose)
  implementation(libs.timber)

  implementation(libs.kotlinx.collection)
}
