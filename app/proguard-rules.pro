-keep class com.google.**  { *; } # gson
-keep interface com.google.**  { *; } # gson
-keep class com.squareup.**  { *; } # okhttp
-keep interface com.squareup.**  { *; } # okhttp
-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}
-dontwarn org.apache.tools.ant.**
-dontwarn okio.**

# Retrofit 2.X
## https://square.github.io/retrofit/ ##

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
