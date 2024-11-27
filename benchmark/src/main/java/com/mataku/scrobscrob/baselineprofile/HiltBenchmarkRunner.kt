package com.mataku.scrobscrob.baselineprofile

import android.app.Application
import android.content.Context
import androidx.benchmark.junit4.AndroidBenchmarkRunner
import dagger.hilt.android.testing.HiltTestApplication

class HiltBenchmarkRunner  : AndroidBenchmarkRunner() {
  override fun newApplication(
    cl: ClassLoader?,
    className: String?,
    context: Context?
  ): Application {
    return super.newApplication(cl, HiltTestApplication::class.java.name, context)
  }
}
