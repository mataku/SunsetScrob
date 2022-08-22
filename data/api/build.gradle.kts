import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("com.google.dagger.hilt.android")
  id("kotlinx-serialization")
}

android {
  defaultConfig {
    val properties = gradleLocalProperties(rootDir)
    val apiKey = properties.getProperty("API_KEY")
    val sharedSecret = properties.getProperty("SHARED_SECRET")
    buildConfigField("String", "API_KEY", apiKey)
    buildConfigField("String", "SHARED_SECRET", sharedSecret)
  }
}

dependencies {
  implementation(libs.hilt.android)
  kapt(libs.hilt.compiler)
  implementation(libs.coroutines)
  implementation(libs.serialization.json)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)
  implementation(libs.ktor.client.json)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.content.negotiation)
}
