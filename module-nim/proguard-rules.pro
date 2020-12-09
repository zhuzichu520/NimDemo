# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in groovy.nodsl.build.kotlin.gradle.kts.txt.ttt.txt
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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

#noinspection ShrinkerUnresolvedReference
#-----------------------------androidx 保留部分------------------------------------
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**

#------------------------------基本设置-------------------------------
-optimizationpasses 5                                                           # 迭代优化次数

-dontusemixedcaseclassnames                                                     # 混淆时不会产生混合的类名

-dontskipnonpubliclibraryclasses                                                # 指定不去忽略非公共的库类

-dontpreverify                                                                  # 不做预校验

-verbose                                                                        # 混淆时记录日志

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所优化规则

-printmapping proguardMapping.txt                                               # 映射关系列表文件

-dontskipnonpubliclibraryclassmembers                                           # 指定不去忽略非公共的库类的成员

-keepattributes *Annotation*,InnerClasses                                       # 保护注解，保护内部类
-keepattributes Signature                                                       # 保护泛型
-keepattributes SourceFile,LineNumberTable                                      # 保持代码行号
-keep public class * extends java.lang.Exception                                # 保持自定义异常


#-----------------------------保留部分------------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep class android.support.** {*;}                                             # 保持support包

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keep class * implements android.os.Parcelable { *;}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# R文件不混淆
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}

# 数据层
-keep class **.entity.** { *;}
-keep class **.arg.** { *;}

-libraryjars <java.home>/lib/rt.jar(java/**,javax/security/**,javax/activation/**,javax/lang/**)

-keep class **$Properties
-keep class org.sqlite.** { *; }
-keep public class android.database.sqlite.**
-keep class com.db.models.**
-keepclassmembers class com.db.models.** { *; }

#MenuBuilder
-keep class * extends android.support.v7.internal.view.menu.MenuBuilder
-keep class * implements android.support.v7.internal.view.menu.MenuBuilder
-keep class android.support.v7.internal.view.menu.MenuBuilder

#---------------------------------webview-----------------------------------
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class com.jollycorp.android.shop.ui.instyle.book.JsInterfaceBook {
   public *;
}
-keepattributes *JavascriptInterface*
-keepclassmembers class * extends android.webkit.webViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
    public void *(android.webkit.WebView, java.lang.String);
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

#---------------------------------log-----------------------------------
-keep class ch.qos.logback.** {*;}
-keep class org.slf4j.** {*;}

#---------------------------------rxhttp-----------------------------------
-keep class rxhttp.**{*;}

-keep class timber.**{*;}

#---------------------------------Aroute-----------------------------------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider

# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
# -keep class * implements com.alibaba.android.arouter.facade.template.IProvider

#---------------------------------友盟-----------------------------------
-keep class com.umeng.** {*;}
-keep class com.uc.** {*;}
-keep class com.zui.** {*;}
-keep class com.miui.** {*;}
-keep class com.heytap.** {*;}
-keep class a.** {*;}
-keep class com.vivo.** {*;}

#高德地图
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
-keep class com.amap.api.trace.**{*;}
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.loc.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
-keep class com.amap.api.services.**{*;}
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

#TBS
-keep class com.tencent.smtt.**{*;}
-keep class com.tencent.tbs.**{*;}

#
-dontwarn com.netease.**
-keep class com.netease.** {*;}
#如果你使用全文检索插件，需要加入
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}

