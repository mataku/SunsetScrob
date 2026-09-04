import java.util.Properties

plugins {
  id("sunsetscrob.library")
  id("sunsetscrob.metro")
}

val localProperties = Properties().apply {
  val file = rootProject.file("local.properties")
  if (file.exists()) file.inputStream().use { load(it) }
}

fun String?.toKotlinStringLiteral(): String {
  val trimmed = (this ?: "").trim()
  val unquoted = if (trimmed.length >= 2 && trimmed.startsWith("\"") && trimmed.endsWith("\"")) {
    trimmed.substring(1, trimmed.length - 1)
  } else {
    trimmed
  }
  val escaped = unquoted
    .replace("\\", "\\\\")
    .replace("\"", "\\\"")
    .replace("$", "\\$")
  return "\"$escaped\""
}

val generatedCredentialsDir = layout.buildDirectory.dir("generated/lastfm/commonMain/kotlin")

abstract class GenerateLastFmApiCredentialsTask : DefaultTask() {
  @get:Input
  abstract val apiKey: Property<String>

  @get:Input
  abstract val sharedSecret: Property<String>

  @get:OutputDirectory
  abstract val outputDir: DirectoryProperty

  @TaskAction
  fun generate() {
    val file = outputDir.get().file("com/mataku/scrobscrob/data/api/LastFmApiCredentials.kt").asFile
    file.parentFile.mkdirs()
    file.writeText(
      """
      package com.mataku.scrobscrob.data.api

      object LastFmApiCredentials {
        const val API_KEY: String = ${apiKey.get()}
        const val SHARED_SECRET: String = ${sharedSecret.get()}
      }
      """.trimIndent()
    )
  }
}

val generateLastFmApiCredentials = tasks.register<GenerateLastFmApiCredentialsTask>("generateLastFmApiCredentials") {
  apiKey.set(localProperties.getProperty("API_KEY").toKotlinStringLiteral())
  sharedSecret.set(localProperties.getProperty("SHARED_SECRET").toKotlinStringLiteral())
  outputDir.set(generatedCredentialsDir)
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
