package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension<*, *, *, *>.testConfiguration() {
  testOptions {
    unitTests {
      isIncludeAndroidResources = true
      all {
        it.maxParallelForks = 2
      }
    }
  }
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }
}
