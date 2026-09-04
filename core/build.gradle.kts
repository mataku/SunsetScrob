plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.compose")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.core"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.coroutines.core)
      implementation(libs.kotlinx.collections.immutable)
      implementation(libs.serialization.json)
    }
  }
}
