# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\adt-bundle-windows-x86_64-20140702\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

## Retrolambda ##
## https://github.com/evant/gradle-retrolambda#proguard ##

-dontwarn java.lang.invoke.*
-dontwarn **$$Lambda$*

## Icepick ##
## https://github.com/frankiesardo/icepick#proguard ##

-dontwarn icepick.**
-keep class icepick.** { *; }
-keep class **$$Icepick { *; }
-keepclasseswithmembernames class * {
    @icepick.* <fields>;
}

-keepnames class * { @icepick.State *; }

## logback ##
## https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-logback-android.pro ##

-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-dontwarn ch.qos.logback.core.net.*

## Crashlytics ##
## https://docs.fabric.io/android/crashlytics/dex-and-proguard.html#exclude-crashlytics-with-proguard ##

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

## Ucrop ##
## https://github.com/Yalantis/uCrop#usage ##

-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

## Other ##

-dontwarn okio.**
