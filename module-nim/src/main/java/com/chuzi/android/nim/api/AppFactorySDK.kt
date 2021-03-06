package com.chuzi.android.nim.api

import android.app.Activity
import android.app.Application
import android.content.res.Configuration
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.alibaba.android.arouter.launcher.ARouter
import com.amap.api.location.AMapLocationClient
import com.amap.api.maps.MapsInitializer
import com.amap.api.services.core.ServiceSettings
import com.chuzi.android.libs.tool.sp2px
import com.chuzi.android.libs.tool.toCast
import com.chuzi.android.mvvm.Mvvm
import com.chuzi.android.nim.R
import com.chuzi.android.nim.core.attachment.NimAttachParser
import com.chuzi.android.nim.core.config.NimConfigSDKOption
import com.chuzi.android.nim.core.error.NimError
import com.chuzi.android.nim.core.error.NimThrowable
import com.chuzi.android.nim.core.event.LoginSyncDataStatusObserver
import com.chuzi.android.nim.core.event.NimEventManager
import com.chuzi.android.nim.ext.authService
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.push.NimMixPushMessageHandler
import com.chuzi.android.shared.BuildConfig
import com.chuzi.android.shared.ext.updateApplicationLanguage
import com.chuzi.android.shared.global.AppGlobal
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.rxhttp.RxHttpManager
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.shared.storage.AppStorage
import com.chuzi.android.widget.crash.CrashConfig
import com.heytap.msp.push.HeytapPushManager
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.RequestCallback
import com.netease.nimlib.sdk.auth.LoginInfo
import com.netease.nimlib.sdk.mixpush.NIMPushClient
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.util.NIMUtil
import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager
import jonathanfinerty.once.Once


/**
 * desc 总集调用的Api
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
object AppFactorySDK {

    /**
     * 是否所有图片置灰
     */
    @JvmField
    val isGrayImage = MutableLiveData<Boolean>()

    /**
     * 消息未读数
     */
    @JvmField
    val unReadNumber: MutableLiveData<Int> = MutableLiveData()

    /**
     * 最近会话livedata对象，在会话列表中维护该集合
     * @see com.chuzi.android.nim.ui.session.fragment.FragmentSession
     */
    val sessionLiveData = MutableLiveData<List<RecentContact>>(listOf())

    /**
     * 聊天界面字体大小 单位ps
     */
    val fontSizeOffset = MutableLiveData<Float>()

    /**
     * 字体偏移大小集合
     */
    val fontSizeOffsetList = listOf(-2f, 0f, 3f, 6f, 10f, 14f)

    /**
     * 开放接口，SDK调用总集
     */
    lateinit var openApi: OpenApi

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
        AppGlobal.init(context)
        //初始化Once
        Once.initialise(context)
        //初始化Http
        RxHttpManager(context)
        CrashConfig.Builder.create().apply()
        if (BuildConfig.DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //设置loading布局样式
        Mvvm.loadingLayoutId = R.layout.layout_hub_loading
        QMUISwipeBackActivityManager.init(context)
        //初始化路由
        ARouter.init(context)
        //初始化皮肤
        SkinManager.install(context)
        //更新语言
        context.updateApplicationLanguage(AppStorage.language)
        //初始化地图
        val mapKey =
            if (BuildConfig.DEBUG) BuildConfig.AMAP_APPKEY_DEBUG else BuildConfig.AMAP_APPKEY_RELEASE
        MapsInitializer.setApiKey(mapKey)
        ServiceSettings.getInstance().setApiKey(mapKey)
        AMapLocationClient.setApiKey(mapKey)

        isGrayImage.value = AppStorage.isGray
        fontSizeOffset.value = fontSizeOffsetList[AppStorage.fontLevel]

        NIMClient.init(context, getLoginInfo(), NimConfigSDKOption.getSDKOptions(context))
        if (NIMUtil.isMainProcess(context)) {
            HeytapPushManager.init(context, true)
            //开启数据同步监听
            NIMClient.toggleNotification(AppStorage.notifyToggle)
            NIMPushClient.registerMixPushMessageHandler(NimMixPushMessageHandler())
            // 注册自定义消息附件解析器
            msgService().registerCustomAttachmentParser(NimAttachParser())
            LoginSyncDataStatusObserver.registerLoginSyncDataStatus(true)
            NimEventManager.registerObserves(true)
        }
    }

    /**
     * 获取IM登录信息
     */
    private fun getLoginInfo(): LoginInfo? {
        val account = AppStorage.account
        val token = AppStorage.token
        if (!account.isNullOrEmpty() && !token.isNullOrEmpty()) {
            return LoginInfo(account, token)
        }
        return null
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
    fun login(account: String, token: String? = null, nimCallBack: NimCallBack<String>) {
        authService().login(LoginInfo(account, token))
            .setCallback(object : RequestCallback<LoginInfo> {
                override fun onSuccess(param: LoginInfo?) {
                    if (param != null) {
                        AppStorage.login(param)
                        nimCallBack.onSuccess(param.token)
                    } else {
                        nimCallBack.onFail(-1, "用户信息空异常")
                    }
                }

                override fun onFailed(code: Int) {
                    val throwable = NimThrowable(code, NimError.toMessage(code))
                    nimCallBack.onFail(code, throwable.message)
                }

                override fun onException(exception: Throwable?) {
                    nimCallBack.onFail(-1, "出异常了")
                    exception?.printStackTrace()
                }
            })
    }

    /**
     * 退出IM释放资源
     */
    @JvmStatic
    fun logout() {
        unReadNumber.value = 0
        sessionLiveData.value = listOf()
        authService().logout()
        AppStorage.logout()
    }

    /**
     * 使用SDK中登录
     */
    fun startLoginActivity(activity: Activity) {
        ARouter.getInstance().build(RoutePath.Login.ACTIVITY_LOGIN_MAIN).navigation()
        activity.finish()
    }

    /**
     * 皮肤切换
     */
    fun applyConfigurationChanged(newConfig: Configuration) {
        SkinManager.applyConfigurationChanged(newConfig)
    }

    /**
     * 获取字体大小，当前默认字体
     */
    fun getFontSizeLiveData(standard: Float): LiveData<Int> {
        return Transformations.map(fontSizeOffset) {
            sp2px(context, standard) + sp2px(context, it)
        }
    }

    /**
     * 获取图片颜色是否置灰
     */
    fun getGrayImageLiveData(): LiveData<Boolean> {
        return Transformations.map(isGrayImage) { it }
    }

}