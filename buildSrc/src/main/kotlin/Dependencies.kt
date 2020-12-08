/**
 * desc
 * author: 朱子楚
 * time: 2020/4/8 2:07 PM
 * since: v 1.0.0
 */

/**
 * 依赖版本
 */
object Versions {
    const val ANDROIDX_TEST_EXT = "1.1.1"
    const val ANDROIDX_TEST = "1.2.0"
    const val APPCOMPAT = "1.2.0"
    const val EXIFINTERFACE = "1.2.0"
    const val RECYCLERVIEW = "1.1.0"
    const val CORE_KTX = "1.3.1"
    const val FRAGMENT = "1.3.0-alpha05"
    const val ESPRESSO_CORE = "3.2.0"
    const val JUNIT = "4.13"
    const val KTLINT = "0.36.0"

    const val OKHTTP = "4.8.1"

    const val RXJAVA = "3.0.6"
    const val RXANDROID = "3.0.0"
    const val RXBINDING = "4.0.0"
    const val RXHTTP = "2.3.5"
    const val RXLIFE = "3.0.0"
    const val RXPERMISSIONS = "0.12"

    const val MATERIAL = "1.2.0"

    const val BINDING_COLLECTION_ADAPTER = "4.0.0"
    const val BINDING_COLLECTION_ADAPTER_PAGING = "3.1.1"

    const val TIMBER = "4.7.1"
    const val LOGBACK = "2.0.0"
    const val SLF4J = "1.7.30"

    const val SWIPEREFRESHLAYOUT = "1.1.0"
    const val FLEXBOX = "2.0.1"
    const val CONSTRAINTLAYOUT = "2.0.0"

    const val MMKV = "1.2.2"
    const val MULTIDEX = "2.0.1"
    const val ONCE = "1.3.0"
    const val AUTOSIZE = "1.2.1"

    const val GUAVA = "27.0.1-android"

    const val EASYFLOAT = "1.3.3"

    const val GSON = "2.8.6"

    const val FASTJSON = "1.2.68"

    const val AROUTE_API = "1.5.1"

    const val AROUTE_COMPILER = "1.5.1"

    const val QMUI = "2.0.0-alpha10"

    const val DEVELOPER = "3.1.9"

    const val COIL = "0.13.0"

    const val JSOUP = "1.12.1"

    const val ROOM = "2.2.5"

    const val RECYCLICAL = "1.1.1"

    const val PAGING = "2.1.2"

    const val UMSDK_COMMON = "9.3.0"

    const val UMSDK_ASMS = "1.1.3"

    const val UMSDK_CRASH = "0.0.4"

    const val NIM = "8.1.2"

    const val AMAP = "latest.integration"

}

/**
 * 高德地图
 */
object AmapLibs {
    const val AMAP_3D = "com.amap.api:3dmap:${Versions.AMAP}"
    const val AMAP_LOCATION = "com.amap.api:location:${Versions.AMAP}"
    const val AMAP_SEARCH = "com.amap.api:search:${Versions.AMAP}"
}

/**
 * 云信IM
 */
object NimLibs {
    const val NIM_BASE = "com.netease.nimlib:basesdk:${Versions.NIM}"
    const val NIM_NRTC = "com.netease.nimlib:nrtc:${Versions.NIM}"
    const val NIM_AVCHAT = "com.netease.nimlib:avchat:${Versions.NIM}"
    const val NIM_CHATROOM = "com.netease.nimlib:chatroom:${Versions.NIM}"
    const val NIM_RTS = "com.netease.nimlib:rts:${Versions.NIM}"
    const val NIM_LUCENE = "com.netease.nimlib:lucene:${Versions.NIM}"
    const val NIM_PUSH = "com.netease.nimlib:push:${Versions.NIM}"
}

/**
 * 插件版本
 */
object BuildPluginsVersion {
    const val AGP = "4.2.0-alpha16"
    const val DETEKT = "1.7.4"
    const val KOTLIN = "1.4.20"
    const val KTLINT = "9.2.1"
    const val VERSIONS_PLUGIN = "0.29.0"
    const val ANDROID_MAVEN = "2.1"
}

/**
 * 插件库
 */
object ClassPaths {
    const val androidBuildTools = "com.android.tools.build:gradle:${BuildPluginsVersion.AGP}"
    const val kotlinGradlePlugin =
        "org.jetbrains.kotlin:kotlin-gradle-plugin:${BuildPluginsVersion.KOTLIN}"
    const val manesPlugin =
        "com.github.ben-manes:gradle-versions-plugin:${BuildPluginsVersion.VERSIONS_PLUGIN}"
    const val dcendentsPlugin =
        "com.github.dcendents:android-maven-gradle-plugin:${BuildPluginsVersion.ANDROID_MAVEN}"
}

/**
 * Android基础库
 */
object SupportLibs {
    const val ANDROIDX_APPCOMPAT = "androidx.appcompat:appcompat:${Versions.APPCOMPAT}"
    const val ANDROIDX_CORE_KTX = "androidx.core:core-ktx:${Versions.CORE_KTX}"
    const val ANDROIDX_FRAGMENT = "androidx.fragment:fragment:${Versions.FRAGMENT}"
    const val ANDROIDX_FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Versions.FRAGMENT}"

    //exifinterface
    const val ANDROIDX_EXIFINTERFACE =
        "androidx.exifinterface:exifinterface:${Versions.EXIFINTERFACE}"
    const val ANDROIDX_RECYCLERVIEW = "androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}"
    const val ANDROIDX_CONSTRAINTLAYOUT =
        "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINTLAYOUT}"
    const val ANDROIDX_SWIPEREFRESHLAYOUT =
        "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.SWIPEREFRESHLAYOUT}"
}

/**
 * 测试库
 */
object TestingLibs {
    const val JUNIT = "junit:junit:${Versions.JUNIT}"
}

/**
 * Android测试库
 */
object AndroidTestingLibs {
    const val ANDROIDX_TEST_RULES = "androidx.test:rules:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_RUNNER = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val ANDROIDX_TEST_EXT_JUNIT = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_EXT}"
    const val ESPRESSO_CORE = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO_CORE}"
}

/**
 * 友盟SDK
 */
object UmSdkLibs {
    const val UMSDK_COMMON = "com.umeng.umsdk:common:${Versions.UMSDK_COMMON}"
    const val UMSDK_ASMS = "com.umeng.umsdk:asms:${Versions.UMSDK_ASMS}"
    const val UMSDK_CRASH = "com.umeng.umsdk:crash:${Versions.UMSDK_CRASH}"
}

/**
 * 第三方其他库
 */
object Libs {

    //material
    const val MATERIAL = "com.google.android.material:material:${Versions.MATERIAL}"

    //okhttp
    const val OKHTTP =
        "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"

    //rx
    const val RXHTTP = "com.ljx.rxhttp:rxhttp:${Versions.RXHTTP}"
    const val RXLIFE = "com.ljx.rxlife3:rxlife-rxjava:${Versions.RXLIFE}"
    const val RXJAVA = "io.reactivex.rxjava3:rxjava:${Versions.RXJAVA}"
    const val RXANDROID = "io.reactivex.rxjava3:rxandroid:${Versions.RXANDROID}"
    const val RXBINDING_CORE = "com.jakewharton.rxbinding4:rxbinding-core:${Versions.RXBINDING}"
    const val RXBINDING_VIEWPAGER =
        "com.jakewharton.rxbinding4:rxbinding-viewpager:${Versions.RXBINDING}"
    const val RXPERMISSIONS = "com.github.tbruyelle:rxpermissions:${Versions.RXPERMISSIONS}"

    //adapter
    const val BINDING_COLLECTION_ADAPTER =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter:${Versions.BINDING_COLLECTION_ADAPTER}"
    const val BINDING_COLLECTION_ADAPTER_RECYCLERVIEW =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-recyclerview:${Versions.BINDING_COLLECTION_ADAPTER}"
    const val BINDING_COLLECTION_ADAPTER_PAGING =
        "me.tatarka.bindingcollectionadapter2:bindingcollectionadapter-paging:${Versions.BINDING_COLLECTION_ADAPTER_PAGING}"

    //log
    const val TIMBER =
        "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val LOGBACK_ANDROID = "com.github.tony19:logback-android:${Versions.LOGBACK}"
    const val SLF4J = "org.slf4j:slf4j-api:${Versions.SLF4J}"

    //ui
    const val FLEXBOX = "com.google.android:flexbox:${Versions.FLEXBOX}"

    //autosize
    const val AUTOSZIE = "me.jessyan:autosize:${Versions.AUTOSIZE}"

    //once
    const val ONCE = "com.jonathanfinerty.once:once:${Versions.ONCE}"

    //multidex
    const val MULTIDEX = "androidx.multidex:multidex:${Versions.MULTIDEX}"

    //mmkv
    const val MMKV = "com.tencent:mmkv-static:${Versions.MMKV}"

    //guava
    const val GUAVA = "com.google.guava:guava:${Versions.GUAVA}"

    const val EASYFLOAT = "com.github.princekin-f:EasyFloat:${Versions.EASYFLOAT}"

    const val GSON = "com.google.code.gson:gson:${Versions.GSON}"

    const val FASTJSON = "com.alibaba:fastjson:${Versions.FASTJSON}"

    const val AROUTER_API = "com.alibaba:arouter-api:${Versions.AROUTE_API}"

    const val QMUI = "com.qmuiteam:qmui:${Versions.QMUI}"

    const val QMUI_ARCH = "com.qmuiteam:arch:${Versions.QMUI}"

    const val COIL = "io.coil-kt:coil:${Versions.COIL}"

    const val JSOUP = "org.jsoup:jsoup:${Versions.JSOUP}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"

    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"

    const val RECYCLICAL = "com.afollestad:recyclical:${Versions.RECYCLICAL}"

}


/**
 * 注解库
 */
object Kapts {
    //rx
    const val RXHTTP_COMPILER = "com.ljx.rxhttp:rxhttp-compiler:${Versions.RXHTTP}"

    const val AROUTER_COMPILER = "com.alibaba:arouter-compiler:${Versions.AROUTE_COMPILER}"

    const val QMUI_ARCH_COMPILER = "com.qmuiteam:arch-compiler:${Versions.QMUI}"

    const val ROOM_COMPILER = "androidx.room:room-compiler:${Versions.ROOM}"
}

/**
 * 个人库
 */
object MyLibs {
    const val DEVELOPER_WIDGET =
        "com.github.zhuzichu520.Developer:library-widget:${Versions.DEVELOPER}"

    const val DEVELOPER_LIBS =
        "com.github.zhuzichu520.Developer:library-libs:${Versions.DEVELOPER}"

    const val DEVELOPER_MVVM =
        "com.github.zhuzichu520.Developer:library-mvvm:${Versions.DEVELOPER}"
}