import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import ext.androidLintConfiguration
import ext.kotlinConfiguration
import ext.testConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlin.kapt")
      }

      extensions.configure<BaseAppModuleExtension> {
        defaultConfig.targetSdk = 31
        signingConfigs {
          getByName("debug") {
            storeFile = file("../debug.keystore")
          }
          create("release") {
            storeFile = file("../SunsetScrob.jks")
            storePassword = System.getenv("SUNSET_STORE_PASSWORD")
            keyAlias = System.getenv("SUNSET_KEY_ALIAS")
            keyPassword = System.getenv("SUNSET_KEY_PASSWORD")
          }
        }
        buildTypes {
          getByName("debug") {
            isMinifyEnabled = true
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
          }
          release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
          }
        }
        kotlinConfiguration()
        androidLintConfiguration()
        packagingOptions {
          val excludePatterns = listOf(
            "META-INF/atomicfu.kotlin_module",
            "META-INF/kotlinx-coroutines-io.kotlin_module",
            "META-INF/kotlinx-io.kotlin_module",
            "META-INF/ktor-client-json.kotlin_module",
            "META-INF/ktor-client-core.kotlin_module",
            "META-INF/ktor-http.kotlin_module",
            "META-INF/ktor-utils.kotlin_module",
            "META-INF/kotlinx-coroutines-core.kotlin_module",
            "META-INF/kotlinx-serialization-runtime.kotlin_module",
            "META-INF/gradle/incremental.annotation.processors"
          )
          resources.excludes.addAll(excludePatterns)
        }
        defaultConfig {
          applicationId = "com.mataku.scrobscrob"
          versionCode = 101
          versionName = "1.0.1"
          proguardFiles(
            getDefaultProguardFile("proguard-android.txt"),
            "proguard-rules.pro"
          )
        }
        testConfiguration()
      }
    }
  }
}