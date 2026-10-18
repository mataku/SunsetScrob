# Applied only to the smoke build type. AGP leaves out of the instrumentation APK every library
# class the app already ships and shrinks the instrumentation APK against this build's mapping,
# so whatever the tests call that the app itself never calls would be removed or merged away by
# R8 and fail at runtime with NoClassDefFoundError / NoSuchMethodError.

# resetDataStores() logs out through the running graph between tests.
-keep class com.mataku.scrobscrob.app.App { *** getAppGraph*(...); }
-keep interface com.mataku.scrobscrob.app.di.AppGraphContract { *** getSessionRepository(); }
-keep interface com.mataku.scrobscrob.data.repository.SessionRepository { *** logout(); }

# Libraries that compose-ui-test, androidx.test and the tests link against: neither removed,
# merged nor renamed. -applymapping remaps the instrumentation APK's class references but not every
# member reference, so allowobfuscation left calls such as
# ContinuationInterceptor$DefaultImpls.releaseInterceptedContinuation pointing at a renamed method.
# Everything else, including our code, Material 3, foundation, Ktor, kotlinx.serialization,
# Metro, Navigation 3 and Coil, is shrunk and optimized exactly as in release.
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.activity.** { *; }
-keep class androidx.collection.** { *; }
-keep class androidx.concurrent.futures.** { *; }
-keep class androidx.core.** { *; }
-keep class androidx.lifecycle.** { *; }
-keep class androidx.tracing.** { *; }
-keep class com.google.common.util.concurrent.** { *; }
-keep class kotlin.** { *; }
-keep class kotlinx.coroutines.** { *; }
