plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.dagger")
  id("sunsetscrob.android.test.screenshot")
  id("io.github.takahirom.roborazzi")
}

android {
  buildFeatures {
    buildConfig = true
  }
  namespace = "com.mataku.scrobscrob.account"
}

dependencies {
  implementation(project(":ui_common"))
  implementation(project(":core"))
  implementation(project(":data:repository"))

  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)
  implementation(libs.compose.material.icons.extended)

  implementation(libs.hilt.navigation.compose)

  implementation(libs.coroutines)
  implementation(libs.timber)


  implementation(libs.accompanist.systemuicontroller)

  implementation(libs.app.update)

  implementation(libs.kotlinx.collection)
}
