import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.metro")
  id("sunsetscrob.android.test.screenshot")
}

configure<LibraryExtension>() {
  namespace = "com.mataku.scrobscrob.home"
}


dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(project(":feature:scrobble"))
  implementation(project(":feature:album"))
  implementation(project(":feature:artist"))


  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.foundation)


  implementation(libs.coroutines)
  implementation(libs.timber)
  implementation(libs.kotlinx.collection)
}
