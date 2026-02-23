import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlinx-serialization")
}

configure<LibraryExtension>() {
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
