import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
  id("sunsetscrob.android.feature")
  id("dagger.hilt.android.plugin")
  id("sunsetscrob.android.dagger")
  id("kotlinx-serialization")
}

android {
  buildFeatures {
    buildConfig = true
  }
  defaultConfig {
    val properties = gradleLocalProperties(rootDir, providers)
    val apiKey = properties.getProperty("API_KEY")
    val sharedSecret = properties.getProperty("SHARED_SECRET")
    buildConfigField("String", "API_KEY", apiKey)
    buildConfigField("String", "SHARED_SECRET", sharedSecret)
  }
  namespace = "com.mataku.scrobscrob.data.api"

  sourceSets {
    getByName("test").resources.srcDirs("src/test/assets")
  }
}

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

  implementation(libs.coil.core)
  implementation(libs.coil.okhttp)
}
