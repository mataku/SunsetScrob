plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlin-android")
  id("com.google.devtools.ksp")
}

android {
  namespace = "com.mataku.scrobscrob.ui_common"

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(project(":core"))
  implementation(libs.activity.compose)
  implementation(libs.compose.ui.tooling)
  implementation(libs.compose.animation)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.accompanist.systemuicontroller)

  implementation(libs.glide.compose)
  implementation(libs.compose.material.icons.extended)

  implementation(libs.showkase.annotation)
  kspDebug(libs.showkase.processor)
  implementation(libs.showkase)
}

ksp {
  arg("skipPrivatePreviews", "true")
}
