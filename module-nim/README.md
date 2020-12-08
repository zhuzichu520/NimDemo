# IM Sdk

NIM SDK 只需几行代码即可实现聊天通信功能，所有页面都是以Fragment为主，可以直接嵌套在Activity或者Fragment页面中。所以总集只需关注两个类即可，一个是AppFactorySdk，这个主要是总集调用SDK，另一个是OpenApi，这个类是个接口，需要在总集写个类去实现这个接口，主要用于SDK调用总集，这个OpenApi接口后续可能会根据业务功能添加方法，所以它是为总集专门定制的接口

## 传送门

 - [SDK开发环境](SDK开发环境)
 - [依赖方式](依赖方式)
 - [使用方法](使用方法)
 - [核心类代码](核心类代码)

## SDK开发环境

Android Studio4.1
targetSdkVersion30
Gradle6.7.1
Androidx

SDK依赖的第三方库有如下代码：

```kotlin
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

    const val AROUTE_API = "1.5.1"

    const val AROUTE_COMPILER = "1.5.1"

    const val QMUI = "2.0.0-alpha10"

    const val DEVELOPER = "3.1.5"

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

    const val AROUTER_API = "com.alibaba:arouter-api:${Versions.AROUTE_API}"

    const val QMUI = "com.qmuiteam:qmui:${Versions.QMUI}"

    const val QMUI_ARCH = "com.qmuiteam:arch:${Versions.QMUI}"

    const val COIL = "io.coil-kt:coil:${Versions.COIL}"

    const val JSOUP = "org.jsoup:jsoup:${Versions.JSOUP}"

    const val ROOM_RUNTIME = "androidx.room:room-runtime:${Versions.ROOM}"

    const val ROOM_KTX = "androidx.room:room-ktx:${Versions.ROOM}"

    const val RECYCLICAL = "com.afollestad:recyclical:${Versions.RECYCLICAL}"

    const val PAGING = "androidx.paging:paging-runtime:${Versions.PAGING}"
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
```

## 依赖方式

我们会提供一个`nimSdk.aar`文件给总集，总集将该`aar`文件添加到`libs`目录，然后在`build.gradle`中依赖`aar`，代码如下

```groovy
dependencies {
		...
		implementation(name: 'nimSdk', ext: 'aar')
    ...
}
```
## 使用方法

1. 在Application中初始化SDK

```kotlin
class ApplicationDemo : Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化SDK
        AppFactorySDK.init(this, OpenApiImpl())

    }

}
```

2. 登录IM

```kotlin
AppFactorySDK.loginNim(account, token, object : Callback<String> {
	/**
	* 登录成功
	*/
	override fun onSuccess(result: String?) {
	}

/**
	* 登录失败
	*/
	override fun onFail(code: Int, message: String?) {
	}
})
```

3. 退出IM

```kotlin
AppFactorySDK.logoutNim()
```

4. 获取沟通，通讯录，设置页面

```kotlin
/**
* 获取沟通页面
*/
AppFactorySDK.getFragment(EnumPage.SESSION)

/**
* 获取联系人页面
*/
AppFactorySDK.getFragment(EnumPage.CONTRACT)

/**
* 获取设置页面
*/
AppFactorySDK.getFragment(EnumPage.SETTING)
```

5. 获取消息未读数消息

```kotlin
/**
* 获取消息未读数
*/
AppFactorySDK.getUnReadNumberLiveData().value

/**
* 监听消息未读数消息变化
*/
AppFactorySDK.getUnReadNumberLiveData().observe(viewLifecycleOwner) { 		number ->
	//处理消息未读数
            
}
```

## 核心类代码

AppFactorySDK.kt

```kotlin
package com.chuzi.android.nim.api

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.chuzi.android.libs.tool.toCast

/**
 * desc 总集调用的Api
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
object AppFactorySDK {

    /**
     * 消息未读数
     */
    private val unReadNumber: LiveData<Int> = MutableLiveData()

    /**
     * 开放接口，SDK调用总集
     */
    private lateinit var openApi: OpenApi

    /**
     * Application上下文
     */
    private lateinit var context: Application

    /**
     * 初始化SDK，请在Application中初始化
     */
    @JvmStatic
    fun init(context: Application, openApi: OpenApi) {
        this.context = context
        this.openApi = openApi
    }

    /**
     * 获取Fragment
     * @param page 界面枚举
     * @see EnumPage.SESSION 会话界面
     * @see EnumPage.CONTRACT 通讯录界面
     * @see EnumPage.SETTING 设置界面
     */
    @JvmStatic
    fun getFragment(page: EnumPage): Fragment {
        return ARouter.getInstance().build(page.router).navigation().toCast()
    }

    /**
     * 登录IM
     * @param account 用户账号
     * @param token 用户密码 可以不传 由sdk生成
     * @param callback 回调接口，成功会返回一个IM token，失败返回错误信息 以及错误码
     */
    @JvmStatic
    fun loginNim(account: String, token: String? = null, callback: Callback<String>) {

    }

    /**
     * 退出IM释放资源
     */
    @JvmStatic
    fun logoutNim() {

    }

    /**
     * 获取未读数的LiveData对象
     */
    fun getUnReadNumberLiveData(): LiveData<Int> {
        return unReadNumber
    }

}
```

OpenApi.kt
```kotlin
package com.chuzi.android.nim.api

/**
 * desc 调用总集api
 * author: 朱子楚
 * time: 2020/12/3 3:40 PM
 * since: v 1.0.0
 */

interface OpenApi {

    /**
     * 测试Api
     */
    fun testApi(): String

    /**
     * 后续根据业务添加方法
     */
}
```