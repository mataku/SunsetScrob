plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("io.github.takahirom.roborazzi")
}

android {
  namespace = "com.mataku.scrobscrob.chart"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.accompanist.systemuicontroller)
  implementation(libs.hilt.navigation.compose)

  implementation(libs.coroutines)
  implementation(libs.kotlinx.collection)

  testImplementation(libs.robolectric)
  testImplementation(libs.roborazzi)
  testImplementation(libs.androidx.test.ext.junit)
  testImplementation(libs.compose.ui.test.junit4)
  testImplementation(libs.junit.vintage.engine)
  // for robolectric
  debugImplementation(libs.compose.ui.test.manifest)
}
