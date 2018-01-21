# gson
-keep class com.google.**  { *; }
-keep interface com.google.**  { *; }

# okhttp3
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

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}


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
-keep class kotlin.jvm.** { *; }
-keep interface kotlin.jvm.** { *; }
-keep class kotlin.reflect.** { *; }
-keep interface kotlin.reflect.** { *; }

-dontwarn sun.security.**
-keep class sun.security.** { *; }
-keep interface sun.security.** { *; }
-dontwarn org.codehaus.**
-keep class org.codehaus.** { *; }
-keep interface org.codehaus.** { *; }
-dontwarn org.apache.tools.ant.**
