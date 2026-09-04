plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
  id("sunsetscrob.metro")
  id("sunsetscrob.test.screenshot")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.scrobble"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":ui_common"))
      implementation(project(":core"))
      implementation(project(":data:repository"))
      implementation(libs.jetbrains.compose.material.icons.extended)
      implementation(libs.jetbrains.lifecycle.runtime.compose)
      implementation(libs.kotlinx.collections.immutable)
    }
    androidMain.dependencies {
      implementation(libs.lottie.compose)
    }
  }
}
