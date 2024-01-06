plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlinx-serialization")
}

android {
  namespace = "com.mataku.scrobscrob.core"
}

dependencies {
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.logging)
  implementation(libs.coroutines)

  implementation(platform(libs.compose.bom))
  implementation(libs.compose.runtime)
  implementation(libs.kotlinx.collection)
}
