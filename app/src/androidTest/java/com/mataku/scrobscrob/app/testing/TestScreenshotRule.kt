package com.mataku.scrobscrob.app.testing

import android.graphics.Bitmap
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.io.PlatformTestStorageRegistry
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Captures screenshots via [android.app.UiAutomation.takeScreenshot] and
 * publishes them through [PlatformTestStorageRegistry] so AGP/UTP pulls
 * them back to:
 *
 *   `<module>/build/intermediates/managed_device_android_test_additional_output/`
 *   `debugAndroidTest/<deviceTask>/<TestClass>-<method>-<label>.png`
 *
 * Why screenshots instead of `screenrecord`:
 *  - `screenrecord` invoked via [androidx.test.uiautomator.UiDevice.executeShellCommand]
 *    silently fails to connect to SurfaceFlinger from within the
 *    UiAutomation shell context (capability/SELinux constraints), even
 *    on a non-ATD `google_apis` system image. UiAutomation's own
 *    `takeScreenshot` uses a different graph and works reliably.
 *
 * Usage:
 *   - On test failure a `-failed.png` is captured automatically so the
 *     screen state at the point of failure is always attached.
 *   - Call [snap] from inside a `@Test` method to drop additional
 *     captures at arbitrary points, e.g. right before a flaky assert:
 *       `screenshotRule.snap("after_album_tap")`.
 *     Each call writes `<TestClass>-<method>-<label>.png`.
 */
class TestScreenshotRule : TestWatcher() {

  private val instrumentation by lazy { InstrumentationRegistry.getInstrumentation() }
  private val storage by lazy { PlatformTestStorageRegistry.getInstance() }
  private var baseName: String? = null

  override fun starting(description: Description) {
    val testClass = description.testClass?.simpleName ?: "test"
    baseName = "$testClass-${description.methodName}"
  }

  override fun failed(e: Throwable, description: Description) {
    snap("failed")
  }

  fun snap(label: String) {
    val name = baseName ?: return
    val bitmap = instrumentation.uiAutomation.takeScreenshot()
    if (bitmap == null) {
      Log.w(TAG, "takeScreenshot returned null")
      return
    }
    runCatching {
      storage.openOutputFile("$name-$label.png").use { os ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
      }
      Log.i(TAG, "$name-$label.png published (${bitmap.width}x${bitmap.height})")
    }.onFailure {
      Log.e(TAG, "screenshot save failed for $name-$label.png", it)
    }
  }

  private companion object {
    const val TAG = "TestScreenshotRule"
  }
}
