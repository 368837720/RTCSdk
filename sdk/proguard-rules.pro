# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\Administrator\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
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

-dontoptimize
-optimizationpasses 5
-verbose
-printmapping out.map

-dontwarn

# 指定不去忽略非公共库的类成员
-dontskipnonpubliclibraryclassmembers

-keepattributes Exceptions,InnerClasses

# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.yunva.jni.** {*;}

#-keep interface com.yaya.sdk.config.SdkConfig {*;}
-keep public class com.yaya.sdk.tcp.core.UnkownSignalException {*;}
-keep public class com.yaya.sdk.tlv.** {*;}
-keep public class com.yaya.sdk.InitParams {*;}
-keep public class com.yaya.sdk.MLog {*;}
-keep interface com.yaya.sdk.MessageFilter {*;}

-keep interface com.yaya.sdk.RTV {*;}

-keep public class com.yaya.sdk.YayaRTV {
    public static <methods>;
}

-keep enum com.yaya.sdk.RTV$Env {*;}
-keep enum com.yaya.sdk.RTV$Mode {*;}

-keep interface com.yaya.sdk.YayaNetStateListener {*;}

-keep interface com.yaya.sdk.VideoTroopsRespondListener {*;}

-keep class okhttp3.** {*;}
-keep class okio.** {*;}
-keep public class org.codehaus.* { *; }
-keep public class java.nio.* { *; }
-dontwarn okio.**

-keepattributes *Annotation*

-libraryjars E:/adt-bundle-windows-x86_64-20140702/adt-bundle-windows-x86_64-20140702/sdk/platforms/android-23/android.jar

-libraryjars libs/okhttp-3.4.1.jar
-libraryjars libs/okio-1.10.0.jar
-libraryjars libs/tlv.jar