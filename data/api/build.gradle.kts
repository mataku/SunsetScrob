import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("sunsetscrob.android.feature")
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
  namespace = "com.mataku.scrobscrob.data.api"
}

apply(from = "${project.rootDir}/gradle/test_dependencies.gradle")

dependencies {
  implementation(libs.coroutines)
  implementation(libs.serialization.json)
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.logging.interceptor)
  implementation(libs.ktor.client.json)
  implementation(libs.ktor.client.logging)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.koin.android)

  testImplementation(libs.koin.test)
}
