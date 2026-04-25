import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.metro")
}

configure<LibraryExtension>() {
  namespace = "com.mataku.scrobscrob.data.repository"

  sourceSets {
    getByName("test").resources.srcDirs("src/test/assets")
  }
}

dependencies {
  implementation(project(":core"))
  api(project(":data:api"))
  api(project(":data:db"))

  implementation(libs.ktor.client.core)
  implementation(libs.timber)
  implementation(libs.kotlinx.collection)
  testImplementation(libs.ktor.client.mock)
  testImplementation(libs.turbine)

  lintChecks(project(":lint-checks"))
}
