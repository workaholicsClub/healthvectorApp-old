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

## Retrofit ##
## http://square.github.io/retrofit/ ##

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

## OkHttp3 ##
## https://github.com/krschultz/android-proguard-snippets/blob/master/libraries/proguard-square-okhttp3.pro ##

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

## Okio ##
## https://github.com/square/okio#proguard ##

-dontwarn okio.**

## Simple Xml ##
## https://stackoverflow.com/a/44152535 ##

-dontwarn javax.xml.stream.**

-keep public class org.simpleframework.** { *; }
-keep class org.simpleframework.xml.** { *; }

-keepattributes ElementList, Root

-keepclassmembers class * {
    @org.simpleframework.xml.* *;
}

## Google Drive Api ##
## https://stackoverflow.com/a/14504602 ##

-keep class com.google.** { *;}
-keep interface com.google.** { *;}
-dontwarn com.google.**
