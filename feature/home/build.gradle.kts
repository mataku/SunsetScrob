plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
  id("sunsetscrob.metro")
  id("sunsetscrob.test.screenshot")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.home"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":ui_common"))
      implementation(project(":core"))
      implementation(project(":data:repository"))
      implementation(project(":feature:scrobble"))
      implementation(project(":feature:album"))
      implementation(project(":feature:artist"))
      implementation(libs.jetbrains.lifecycle.runtime.compose)
      implementation(libs.kotlinx.collections.immutable)
    }
  }
}
