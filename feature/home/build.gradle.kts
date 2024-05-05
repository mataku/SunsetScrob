plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("sunsetscrob.android.test.screenshot")
}

android {
  namespace = "com.mataku.scrobscrob.home"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(project(":feature:scrobble"))

  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.hilt.navigation.compose)

  implementation(libs.coroutines)
  implementation(libs.timber)
  implementation(libs.kotlinx.collection)
}
