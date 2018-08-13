# gson
-keep class com.google.**  { *; }
-keep interface com.google.**  { *; }

# okhttp3
-dontwarn okhttp3.internal.platform.*
-keep class okhttp3.**  { *; }
-keep interface okhttp3.**  { *; }
-keep class okio.**  { *; }
-keep interface okio.**  { *; }
-dontwarn okio.**

# realm
-keep class io.realm.** { *; }
-keep interface io.realm.** { *; }

# Rx
-keep class org.reactivestreams.** { *; }
-keep interface org.reactivestreams.** { *; }

-keep class io.reactivex.**  { *; }
-keep interface io.reactivex.**  { *; }
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-dontwarn rx.**

# android specific
-keep class android.support.v4.app.** { *; }
-keep interface android.support.v4.app.** { *; }
-keepattributes *Annotation*

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# other
-keep class com.mataku.scrobscrob.app.model.** { *; }
-keep class kotlin.** { *; }
-keep interface kotlin.** { *; }

-dontwarn sun.security.**
-keep class sun.security.** { *; }
-keep interface sun.security.** { *; }
-dontwarn org.codehaus.**
-keep class org.codehaus.** { *; }
-keep interface org.codehaus.** { *; }
-dontwarn org.apache.tools.ant.**

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-dontwarn com.squareup.moshi.**

# Kotlin Coroutines
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Glide
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep class com.bumptech.glide.RequestManager
-dontwarn com.bumptech.glide.load.resource.bitmap.VideoDecoder

-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
