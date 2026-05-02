package com.mataku.scrobscrob.app.testing

/**
 * Marks an instrumentation test that requires a large-screen (tablet)
 * device to render correctly. Tests with this annotation are excluded
 * from the default `pixel6Api35DebugAndroidTest` run via
 * `testInstrumentationRunnerArguments["notAnnotation"]`. To run them,
 * pass `-PincludeLargeScreenE2E=true` (typically against
 * `pixelTabletApi35DebugAndroidTest`).
 *
 * Lives in `app/src/androidTest/` rather than `:test_helper:integration`
 * because the latter brings Robolectric / Roborazzi into transitive
 * classpath, which corrupts Espresso's idle checks on a real emulator.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class LargeScreenE2E
