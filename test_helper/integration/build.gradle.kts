plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
}

android {
  namespace = "com.mataku.scrobscrob.test_helper.integration"
}

dependencies {
  implementation(project(":core"))
  implementation(project(":ui_common"))
  implementation(libs.compose.ui.test.android)
  implementation(libs.compose.material3)

  implementation(libs.robolectric)
  implementation(libs.roborazzi)
  implementation(libs.androidx.test.ext.junit)
  implementation(libs.compose.ui.test.junit4)
  implementation(libs.junit.vintage.engine)
  // for robolectric
  implementation(libs.compose.ui.test.manifest)
}
