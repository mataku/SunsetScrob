package ext

import com.android.build.api.dsl.CommonExtension

fun CommonExtension.androidSdkConfiguration() {
  compileSdk = 37
  defaultConfig.minSdk = 30
}
