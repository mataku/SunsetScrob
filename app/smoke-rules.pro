# Applied only to the smoke build type. AGP leaves out of the instrumentation APK every library
# class the app already ships and shrinks the instrumentation APK against this build's mapping,
# so whatever the tests call that the app itself never calls would be removed or merged away by
# R8 and fail at runtime with NoClassDefFoundError / NoSuchMethodError.

# resetDataStores() logs out through the running graph between tests.
-keep class com.mataku.scrobscrob.app.App { *** getAppGraph*(...); }
-keep interface com.mataku.scrobscrob.app.di.AppGraphContract { *** getSessionRepository(); }
-keep interface com.mataku.scrobscrob.data.repository.SessionRepository { *** logout(); }

# Libraries that compose-ui-test, androidx.test and the tests link against. They stay renamable
# (the instrumentation APK is remapped with -applymapping) but are neither removed nor merged.
# Everything else, including our code, Material 3, foundation, Ktor, kotlinx.serialization,
# Metro, Navigation 3 and Coil, is shrunk and optimized exactly as in release.
-keep,allowobfuscation class androidx.compose.runtime.** { *; }
-keep,allowobfuscation class androidx.compose.ui.** { *; }
-keep,allowobfuscation class androidx.activity.** { *; }
-keep,allowobfuscation class androidx.collection.** { *; }
-keep,allowobfuscation class androidx.concurrent.futures.** { *; }
-keep,allowobfuscation class androidx.core.** { *; }
-keep,allowobfuscation class androidx.lifecycle.** { *; }
-keep,allowobfuscation class androidx.tracing.** { *; }
-keep,allowobfuscation class com.google.common.util.concurrent.** { *; }
-keep,allowobfuscation class kotlin.** { *; }
-keep,allowobfuscation class kotlinx.coroutines.** { *; }
