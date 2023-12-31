//package com.example.benchmark
//
//import androidx.benchmark.macro.BaselineProfileMode
//import androidx.benchmark.macro.CompilationMode
//import androidx.benchmark.macro.StartupMode
//import androidx.benchmark.macro.StartupTimingMetric
//import androidx.benchmark.macro.junit4.MacrobenchmarkRule
//import org.junit.Rule
//import org.junit.Test
//
//class StartupBenchmark {
//
//  @get:Rule
//  val benchmarkRule = MacrobenchmarkRule()
//
//  @Test
//  fun noBaselineAndCompilation() {
//    benchmarkRule.measureRepeated(
//      packageName = "com.mataku.scrobscrob",
//      metrics = listOf(StartupTimingMetric()),
//      compilationMode = CompilationMode.None(),
//      iterations = 10,
//      startupMode = StartupMode.COLD,
//      setupBlock = {
//        pressHome()
//      }
//    ) {
//      startActivityAndWait()
//    }
//  }
//
//  @Test
//  fun baselineProfileMode() {
//    benchmarkRule.measureRepeated(
//      packageName = "com.mataku.scrobscrob",
//      metrics = listOf(StartupTimingMetric()),
//      compilationMode = CompilationMode.Partial(
//        baselineProfileMode = BaselineProfileMode.Require
//      ),
//      iterations = 10,
//      startupMode = StartupMode.COLD,
//      setupBlock = {
//        pressHome()
//      }
//    ) {
//      startActivityAndWait()
//    }
//  }
//
//  @Test
//  fun fullCompilationMode() {
//    benchmarkRule.measureRepeated(
//      packageName = "com.mataku.scrobscrob",
//      metrics = listOf(StartupTimingMetric()),
//      compilationMode = CompilationMode.Full(),
//      iterations = 10,
//      startupMode = StartupMode.COLD,
//      setupBlock = {
//        pressHome()
//      }
//    ) {
//      startActivityAndWait()
//    }
//  }
//}
