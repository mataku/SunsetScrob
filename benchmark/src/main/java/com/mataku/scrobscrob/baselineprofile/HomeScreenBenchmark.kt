package com.mataku.scrobscrob.baselineprofile

import android.graphics.Point
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeScreenBenchmark : ScreenBenchmark(StartupMode.COLD) {

  override val metrics: List<Metric>
    get() = listOf(
      FrameTimingMetric(),
    )

  override fun MacrobenchmarkScope.setupBlock() {

  }

  override fun MacrobenchmarkScope.measureBlock() {
    startTaskActivity("home")
//    login()
//    Thread.sleep(2_000)
    device.wait(Until.hasObject(By.res("scrobbleList")), 5_000)
    val scrobbleScreen = device.findObject(By.res("scrobbleList"))
    device.waitForIdle()
    scrobbleScreen.setGestureMargin(
      device.displayWidth / 5
    )
    scrobbleScreen.drag(
      Point(scrobbleScreen.visibleCenter.x, scrobbleScreen.visibleCenter.y / 3)
    )
    Thread.sleep(2_000)
    device.waitForIdle()
    scrobbleScreen.fling(Direction.UP)
    device.waitForIdle()
    Thread.sleep(2_000)
    scrobbleScreen.drag(
      Point(scrobbleScreen.visibleCenter.x, scrobbleScreen.visibleCenter.y / 3)
    )
    device.waitForIdle()
    Thread.sleep(2_000)
  }

  @Test
  fun measure() {
    benchmark(CompilationMode.Full())
  }
}
