# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/shwetad/Android/Sdk/tools/proguard/proguard-android.txt
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

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#-injars  in1.jar
#-injars  in2.jar(!META-INF/MANIFEST.MF)
#-injars  in3.jar(!META-INF/MANIFEST.MF)
#-outjars out.jar

#OKHTTP3
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn org.conscrypt.**
-dontwarn com.squareup.okhttp3.**
-dontwarn okhttp3.internal.platform.**
#-keep class com.squareup.okhttp3.** { *; }
#-keep interface com.squareup.okhttp3.** { *; }
#-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.**
-dontwarn javax.annotation.ParametersAreNonnullByDefault

#Java Io
-dontwarn java.io.IOException
-dontwarn kotlin.**
-dontwarn org.jetbrains.annotations.NotNull

#activities
# keep the class and specified members from being removed or renamed
#-keep class com.trimax.vts.mobile.ReplayTrackingActivity { *; }

# keep the specified class members from being removed or renamed
# only if the class is preserved
#-keepclassmembers class com.trimax.vts.mobile.ReplayTrackingActivity { *; }

-keep class com.trimax.**{*;}
-keepnames class com.trimax.**{*;}



#HttpCore
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**

#Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
#-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>



-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.**

-keep class com.google.android.gms.maps.** { *; }
-keep interface com.google.android.gms.maps.** { *; }

#event bus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}