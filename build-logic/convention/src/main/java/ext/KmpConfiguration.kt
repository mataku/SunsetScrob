package ext

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

private const val VRT_TAG = "VRT"
private const val ROBOLECTRIC_VRT_TAG = "com.mataku.scrobscrob.test_helper.integration.VRT"

fun Project.kotlinMultiplatform(): KotlinMultiplatformExtension =
  extensions.getByType<KotlinMultiplatformExtension>()

fun KotlinMultiplatformExtension.androidLibraryTarget(configure: KotlinMultiplatformAndroidLibraryTarget.() -> Unit) {
  (this as ExtensionAware).extensions.configure<KotlinMultiplatformAndroidLibraryTarget>("android") {
    configure()
  }
}

fun KotlinMultiplatformExtension.androidNamespace(): String? =
  ((this as ExtensionAware).extensions.getByName("android") as KotlinMultiplatformAndroidLibraryTarget).namespace

fun Project.kmpTargetsConfiguration() {
  kotlinMultiplatform().apply {
    jvm()
    androidLibraryTarget {
      compileSdk = 37
      minSdk = 30
      compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
      }
    }
  }
}

fun Project.kmpTestConfiguration() {
  val project = this
  tasks.withType(Test::class.java).configureEach {
    maxParallelForks = Runtime.getRuntime().availableProcessors()
    useJUnitPlatform {
      if (project.hasProperty("excludeScreenshotTest")) {
        excludeTags(VRT_TAG, ROBOLECTRIC_VRT_TAG)
      }
      if (project.hasProperty("onlyScreenshotTest")) {
        includeTags(VRT_TAG, ROBOLECTRIC_VRT_TAG)
        excludeEngines("kotest")
      }
    }
  }
}
