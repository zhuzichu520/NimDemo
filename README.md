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


## 依赖方式

将module-nim 编译打包成 `nimSdk.aar`文件给总集，总集将该`aar`文件添加到`libs`目录，然后在`build.gradle`中依赖`aar`，代码如下

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
package com.hiwitech.android.nim.api

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.hiwitech.android.libs.tool.toCast

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
     * @param nimCallBack 回调接口，成功会返回一个IM token，失败返回错误信息 以及错误码
     */
    @JvmStatic
    fun loginNim(account: String, token: String? = null, nimCallBack: Callback<String>) {

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
package com.hiwitech.android.nim.api

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