package ext

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.artifacts.VersionCatalog

fun CommonExtension<*, *, *, *>.composeConfiguration(
  libs: VersionCatalog
) {
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion =
      libs.findVersion("kotlin.compiler.extension").get().toString()
  }
}
