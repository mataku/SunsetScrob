package ext

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.kotlinConfiguration() {
  val type = if (this.name == "app") {
    BaseAppModuleExtension::class.java
  } else {
    LibraryExtension::class.java
  }
  extensions.configure(type) {
    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_17
      targetCompatibility = JavaVersion.VERSION_17
    }
  }

  with(extensions.getByType<KotlinAndroidProjectExtension>()) {
    compilerOptions {
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)

      // painful but too many files target
      freeCompilerArgs.add("-Xopt-in=androidx.compose.animation.ExperimentalSharedTransitionApi")
    }
  }
}
