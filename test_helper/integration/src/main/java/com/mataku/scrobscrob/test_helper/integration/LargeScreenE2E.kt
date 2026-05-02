package com.mataku.scrobscrob.test_helper.integration

/**
 * Marks an instrumentation test that requires a large-screen (tablet)
 * device to render correctly. Tests with this annotation are excluded
 * from the default `pixel6Api35DebugAndroidTest` run via
 * `testInstrumentationRunnerArguments["notAnnotation"]`. To run them,
 * pass `-PincludeLargeScreenE2E=true` (typically against
 * `pixelTabletApi35DebugAndroidTest`).
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LargeScreenE2E
