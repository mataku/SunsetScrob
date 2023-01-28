plugins {
  id("sunsetscrob.android.feature")
}

android {
  namespace = "com.mataku.scrobscrob.test_helper"
}

dependencies {
  implementation(libs.androidx.test.core)
  implementation(libs.kotest.runner.junit5)
  implementation(libs.kotest.assertions)
  implementation(libs.mockk)
  implementation(libs.mockk.agent.jvm)
  implementation(libs.coroutines.test)
  implementation(libs.coroutines)
}
