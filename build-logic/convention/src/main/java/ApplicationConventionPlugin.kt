import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ManagedVirtualDevice
import ext.androidLintConfiguration
import ext.androidSdkConfiguration
import ext.kotlinConfiguration
import ext.testConfiguration
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class ApplicationConventionPlugin : Plugin<Project> {
  private val appVersionName = "1.25.0"

  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
      }

      extensions.configure<ApplicationExtension> {
        androidSdkConfiguration()
        defaultConfig.targetSdk = 36
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
            isMinifyEnabled = false
            applicationIdSuffix = ".dev"
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
          }
          release {
            isMinifyEnabled = true
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
            ndk {
              debugSymbolLevel = "FULL"
            }
          }
        }
        kotlinConfiguration()
        androidLintConfiguration()
        packaging {
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
            "META-INF/gradle/incremental.annotation.processors",
            "META-INF/LICENSE.md",
            "META-INF/LICENSE-notice.md",
            "META-INF/AL2.0",
            "win32-x86-64/attach_hotspot_windows.dll",
            "win32-x86/attach_hotspot_windows.dll",
            "META-INF/licenses/ASM",
            "META-INF/LGPL2.1"
          )
          resources.excludes.addAll(excludePatterns)
        }
        defaultConfig {
          applicationId = "com.mataku.scrobscrob"
          versionName = appVersionName
          versionCode = generateVersionCode(appVersionName)
          testInstrumentationRunner = "com.mataku.scrobscrob.app.testing.MetroTestRunner"
          proguardFiles(
            getDefaultProguardFile("proguard-android-optimize.txt"),
            "proguard-rules.pro"
          )
        }
        sourceSets {
          getByName("androidTest") {
            assets.srcDirs("src/test/assets", "src/androidTest/assets")
          }
        }
        testOptions.managedDevices.allDevices.create(
          "pixel6Api35",
          ManagedVirtualDevice::class.java,
        ) {
          device = "Pixel 6"
          apiLevel = 35
          systemImageSource = "aosp-atd"
        }
      }
      testConfiguration()
    }
  }
}

private fun generateVersionCode(versionName: String): Int {
  val versionCodeDigits = versionName.split(".")
  val major = versionCodeDigits[0].toInt()
  val minor = versionCodeDigits[1].toInt()
  val patch = versionCodeDigits[2].toInt()
  return major * 1000000 + minor * 1000 + patch
}
