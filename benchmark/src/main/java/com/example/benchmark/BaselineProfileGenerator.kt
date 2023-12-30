package com.example.benchmark

import androidx.benchmark.macro.junit4.BaselineProfileRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BaselineProfileGenerator {
  @get:Rule
  val baselineProfileRule = BaselineProfileRule()

  @Test
  fun generateBaselineProfile() {
    baselineProfileRule.collect(packageName = "com.mataku.scrobscrob") {
      pressHome()
      startActivityAndWait()
      device.waitForIdle()
    }
  }
}
