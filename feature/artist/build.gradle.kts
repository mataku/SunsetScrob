import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.metro")
  id("sunsetscrob.android.test.screenshot")
  id("io.github.takahirom.roborazzi")
}

configure<LibraryExtension>() {
  namespace = "com.mataku.scrobscrob.artist"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))
  implementation(libs.activity.compose)
  implementation(libs.compose.foundation)


  implementation(libs.coroutines)

  implementation(libs.kotlinx.collection)

  implementation(libs.compose.ui.tooling)
}
