plugins {
  id("sunsetscrob.android.feature")
  id("sunsetscrob.android.compose")
  id("kotlinx-serialization")
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

android {
  namespace = "com.mataku.scrobscrob.core"
}

dependencies {
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.logging)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)
  implementation(libs.serialization.json)
  implementation(libs.coroutines)

  implementation(platform(libs.compose.bom))
  implementation(libs.compose.runtime)
  implementation(libs.kotlinx.collection)
}

repositories {
  google()
  mavenCentral()
  maven("https://maven.google.com")
  maven("https://kotlin.bintray.com/kotlinx")
}

kapt {
  correctErrorTypes = true

  javacOptions {
    option("-Xmaxerrs", 1000)
  }
}
