plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("sunsetscrob.android.test.screenshot")
}

android {
  namespace = "com.mataku.scrobscrob.album"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.activity.compose)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.hilt.navigation.compose)

  implementation(libs.coroutines)

  implementation(libs.kotlinx.collection)

  implementation(libs.compose.ui.tooling)
}
