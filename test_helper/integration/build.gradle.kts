plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
}

android {
  namespace = "com.mataku.scrobscrob.test_helper.integration"
}

dependencies {
  implementation(libs.compose.ui.test.android)
}
