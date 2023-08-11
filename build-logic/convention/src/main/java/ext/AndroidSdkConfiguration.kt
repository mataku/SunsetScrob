package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension<*, *, *, *, *>.androidSdkConfiguration() {
  compileSdk = 34
  defaultConfig {
    minSdk = 29
  }
}
