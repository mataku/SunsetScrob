package ext

import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

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
}
