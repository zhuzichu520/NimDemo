package com.chuzi.android.nim.api

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.alibaba.android.arouter.launcher.ARouter
import com.chuzi.android.libs.tool.toCast
import com.chuzi.android.mvvm.Mvvm
import com.chuzi.android.nim.BuildConfig
import com.chuzi.android.nim.R
import com.chuzi.android.nim.core.event.LoginSyncDataStatusObserver
import com.chuzi.android.nim.core.attachment.NimAttachParser
import com.chuzi.android.nim.core.config.NimConfigSDKOption
import com.chuzi.android.nim.core.event.NimEventManager
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.shared.global.AppGlobal
import com.chuzi.android.shared.rxhttp.RxHttpManager
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.shared.storage.AppStorage
import com.chuzi.android.widget.crash.CrashConfig
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.LoginInfo
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
     * 消息未读数
     */
    private val unReadNumber: MutableLiveData<Int> = MutableLiveData()

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

        NIMClient.init(context, getLoginInfo(), NimConfigSDKOption.getSDKOptions(context))
        if (NIMUtil.isMainProcess(context)) {
            //开启数据同步监听
            NIMClient.toggleNotification(AppStorage.notifyToggle)
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
    fun getUnReadNumberLiveData(): MutableLiveData<Int> {
        return unReadNumber
    }


}