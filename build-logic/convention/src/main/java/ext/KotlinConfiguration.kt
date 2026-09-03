package ext

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension

fun Project.kotlinConfiguration() {
  extensions.getByType<KotlinAndroidProjectExtension>().compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
  extensions.findByType(ApplicationExtension::class.java)?.apply {
    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17
  }
  extensions.findByType(LibraryExtension::class.java)?.apply {
    compileOptions.sourceCompatibility = JavaVersion.VERSION_17
    compileOptions.targetCompatibility = JavaVersion.VERSION_17
  }
}
