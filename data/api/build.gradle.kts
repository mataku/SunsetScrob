import java.util.Properties

plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.metro")
}

val localProperties = Properties().apply {
  val file = rootProject.file("local.properties")
  if (file.exists()) file.inputStream().use { load(it) }
}
val apiKey = localProperties.getProperty("API_KEY") ?: "\"\""
val sharedSecret = localProperties.getProperty("SHARED_SECRET") ?: "\"\""
val generatedCredentialsDir = layout.buildDirectory.dir("generated/lastfm/commonMain/kotlin")

val generateLastFmApiCredentials by tasks.registering {
  val outDir = generatedCredentialsDir
  inputs.property("apiKey", apiKey)
  inputs.property("sharedSecret", sharedSecret)
  outputs.dir(outDir)
  doLast {
    val file = outDir.get().file("com/mataku/scrobscrob/data/api/LastFmApiCredentials.kt").asFile
    file.parentFile.mkdirs()
    file.writeText(
      """
      package com.mataku.scrobscrob.data.api

      object LastFmApiCredentials {
        const val API_KEY: String = $apiKey
        const val SHARED_SECRET: String = $sharedSecret
      }
      """.trimIndent()
    )
  }
}

kotlin {
  android {
    namespace = "com.mataku.scrobscrob.data.api"
  }

  sourceSets {
    commonMain {
      kotlin.srcDir(generatedCredentialsDir)
      dependencies {
        implementation(libs.coroutines.core)
        implementation(libs.serialization.json)
        implementation(libs.ktor.client.core)
        implementation(libs.ktor.client.json)
        implementation(libs.ktor.client.logging)
        implementation(libs.ktor.client.content.negotiation)
      }
    }
    androidMain.dependencies {
      implementation(project(":core"))
      implementation(libs.coroutines)
      implementation(project.dependencies.platform(libs.okhttp.bom))
      implementation(libs.okhttp)
      implementation(libs.okhttp.logging.interceptor)
      implementation(libs.ktor.client.okhttp)
      implementation(libs.coil.core)
      implementation(libs.coil.okhttp)
    }
    jvmTest.dependencies {
      implementation(project(":core"))
      implementation(libs.ktor.client.mock)
    }
  }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
  dependsOn(generateLastFmApiCredentials)
}
