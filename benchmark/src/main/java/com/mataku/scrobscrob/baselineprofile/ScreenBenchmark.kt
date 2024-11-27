package com.mataku.scrobscrob.baselineprofile

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.Metric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.uiautomator.By
import org.junit.Rule

abstract class ScreenBenchmark(
  protected val startupMode: StartupMode = StartupMode.WARM,
  protected val iterations: Int = 1
) {
  @get:Rule
  val rule = MacrobenchmarkRule()

  abstract val metrics: List<Metric>

  open fun MacrobenchmarkScope.setupBlock() {}
  abstract fun MacrobenchmarkScope.measureBlock()

  fun benchmark(compilationMode: CompilationMode) {
    rule.measureRepeated(
      packageName = "com.mataku.scrobscrob",
      metrics = metrics,
      compilationMode = compilationMode,
      startupMode = startupMode,
      iterations = iterations,
      setupBlock = { setupBlock() },
      measureBlock = { measureBlock() }
    )
  }
}

fun MacrobenchmarkScope.startTaskActivity(task: String) =
  startActivityAndWait { it.putExtra("EXTRA_START_TASK", task) }

fun MacrobenchmarkScope.login() {
  // TODO: Mock the login response using fake repository
  device.findObject(By.res("loginUsername")).text = BuildConfig.USERNAME
  device.findObject(By.res("loginPassword")).text = BuildConfig.PASSWORD
  device.findObject(By.res("loginButton")).click()
}
