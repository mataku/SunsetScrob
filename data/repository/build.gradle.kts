import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
}

configure<LibraryExtension>() {
  namespace = "com.mataku.scrobscrob.data.repository"

  sourceSets {
    getByName("test").resources.srcDirs("src/test/assets")
  }
}

dependencies {
  implementation(project(":core"))
  implementation(project(":data:api"))
  implementation(project(":data:db"))

  implementation(libs.ktor.client.core)
  implementation(libs.timber)
  implementation(libs.kotlinx.collection)
  testImplementation(libs.ktor.client.mock)
  testImplementation(libs.turbine)
}
