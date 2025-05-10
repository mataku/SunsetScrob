plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.test.screenshot")
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
  implementation(libs.compose.animation)
  implementation(libs.compose.material3)
  implementation(libs.compose.navigation)

  implementation(libs.coil.compose)
  implementation(libs.compose.material.icons.extended)

  implementation(libs.showkase.annotation)
  kspDebug(libs.showkase.processor)
  implementation(libs.showkase)
  implementation(libs.kotlinx.collection)

  implementation(libs.compose.ui.tooling)
}

ksp {
  arg("skipPrivatePreviews", "true")
}
