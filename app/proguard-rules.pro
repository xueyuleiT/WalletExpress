# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/cashbus/Android/Sdk/tools/proguard/proguard-android.txt
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

-keep public class com.express.wallet.walletexpress.R$*{
    public static final int *;
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keep class okhttp3.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-dontwarn com.squareup.okhttp3.**
-keep class com.tencent.mm.sdk.** {
   *;
}

#------------  safe java bridge  ----------
#--------------- BEGIN: Gson防混淆 ----------
-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.** { *; }
#--------------- END ----------

#---------------去除log日志----------------
-assumenosideeffects class android.util.Log { public *;}
-ignorewarnings

#---------------jpush----------------
-dontwarn cn.jpush.**

-keep class cn.jpush.** { *; }

#-keep class com.megvii.demo.** {*;}
#-ignorewarnings
#---------------eventbus----------------
-keepclassmembers class ** {
    public void onEvent*(**);
}

-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.app.Application
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
-keep class android.support.v7.internal.view.menu.** { *; }

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep public class com.express.wallet.walletexpress.model.** { *; }
-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}
-keepattributes SourceFile,LineNumberTable