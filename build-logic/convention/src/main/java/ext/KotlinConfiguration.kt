package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun CommonExtension<*, *, *, *>.kotlinConfiguration() {
  compileSdk = 33
  defaultConfig {
    minSdk = 29
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  (this as ExtensionAware).extensions.configure<KotlinJvmOptions>("kotlinOptions") {
    jvmTarget = "1.8"
    freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
  }
}
