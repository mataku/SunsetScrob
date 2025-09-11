package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension<*, *, *, *, *, *>.androidSdkConfiguration() {
  compileSdk = 36
  defaultConfig {
    minSdk = 30
  }
}
