plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
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

  implementation(libs.timber)

  implementation(libs.kotlinx.collection)
}
