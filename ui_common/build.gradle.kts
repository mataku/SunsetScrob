import com.android.build.api.dsl.LibraryExtension

plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("sunsetscrob.android.test.screenshot")
  alias(libs.plugins.kotlin.serialization)
}

configure<LibraryExtension>() {
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
  implementation(libs.compose.material3.adaptive)
  implementation(libs.compose.material3.adaptive.layout)
  implementation(libs.compose.material3.adaptive.navigation)


  implementation(libs.coil.compose)
  implementation(libs.compose.material.icons.extended)

  implementation(libs.kotlinx.collection)

  implementation(libs.compose.ui.tooling)

  implementation(libs.navigation3.runtime)
  implementation(libs.navigation3.ui)
  implementation(libs.serialization.json)
  implementation(libs.metrox.viewmodel.compose)
}
