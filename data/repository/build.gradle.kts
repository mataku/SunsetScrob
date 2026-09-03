plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.metro")
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.data.repository"
  }

  sourceSets {
    commonMain.dependencies {
      implementation(project(":core"))
      api(project(":data:api"))
      api(project(":data:db"))
      implementation(libs.coroutines.core)
      implementation(libs.ktor.client.core)
      implementation(libs.kotlinx.collections.immutable)
    }
    androidMain.dependencies {
      implementation(libs.coroutines)
      implementation(libs.timber)
    }
    jvmTest.dependencies {
      implementation(libs.ktor.client.mock)
    }
  }
}
