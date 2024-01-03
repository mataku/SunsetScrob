plugins {
  id("sunsetscrob.android.feature")
}

android {
  namespace = "com.mataku.scrobscrob.test_helper.unit"

  packaging {
    val excludePatterns = listOf(
      "META-INF/LICENSE.md",
      "META-INF/LICENSE-notice.md",
      "win32-x86-64/attach_hotspot_windows.dll",
      "win32-x86/attach_hotspot_windows.dll",
      "META-INF/AL2.0",
      "META-INF/licenses/ASM",
      "META-INF/LGPL2.1"
    )
    resources.excludes.addAll(excludePatterns)
  }
}

dependencies {
  implementation(libs.kotest.runner.junit5)
  implementation(libs.coroutines.test)
}
