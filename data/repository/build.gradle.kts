plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
}

android {
  namespace = "com.mataku.scrobscrob.data.repository"
}

dependencies {
  implementation(project(":core"))
  implementation(project(":data:api"))
  implementation(project(":data:db"))

  // TODO: Endpoint の requestType を独自の enum にして ktor に依存しないようにした上で消す
  implementation(libs.ktor.client.core)

  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)

  implementation(libs.timber)

  implementation(libs.kotlinx.collection)
}
