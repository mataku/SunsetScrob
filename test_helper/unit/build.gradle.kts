plugins {
  id("sunsetscrob.library")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.test_helper.unit"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.kotest.runner.junit5)
      implementation(libs.coroutines.test)
    }
  }
}
