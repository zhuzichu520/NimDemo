package com.chuzi.android.nim.core.config

import android.content.Context
import com.chuzi.android.nim.ActivityNim
import com.chuzi.android.nim.core.provider.NimUserInfoProvider
import com.chuzi.android.nim.tools.ToolImage.getImageMaxEdge
import com.chuzi.android.shared.global.CacheGlobal
import com.netease.nimlib.sdk.SDKOptions
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.mixpush.MixPushConfig
import com.netease.nimlib.sdk.msg.MessageNotifierCustomization
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc IM初始化参数
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
object NimConfigSDKOption {

    fun getSDKOptions(context: Context): SDKOptions {
        return SDKOptions().apply {
            /**
             * 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
             */
            initStatusBarNotificationConfig(this)
            /**
             * 配置 APP 保存图片/语音/文件/log等数据的目录
             */
            sdkStorageRootPath = CacheGlobal.getNimCacheDir()
            /**
             * 配置是否需要预下载附件缩略图
             */
            preloadAttach = false
            /**
             * 配置附件缩略图的尺寸大小
             */
            thumbnailSize = getImageMaxEdge()
            /**
             * 通知栏显示用户昵称和头像
             */
            userInfoProvider = NimUserInfoProvider(context)
            /**
             * 定制通知栏提醒文案（可选，如果不定制将采用SDK默认文案）
             */
            messageNotifierCustomization = object :
                MessageNotifierCustomization {
                override fun makeNotifyContent(nick: String, message: IMMessage): String? {
                    return null // 采用SDK默认文案
                }

                override fun makeTicker(nick: String, message: IMMessage): String? {
                    return null // 采用SDK默认文案
                }

                override fun makeRevokeMsgTip(revokeAccount: String, item: IMMessage): String {
                    return revokeAccount.plus("撤回了一条消息")
                }
            }
            /**
             * 在线多端同步未读数
             */
            sessionReadAck = true
            /**
             * 动图的缩略图直接下载原图
             */
            animatedImageThumbnailEnabled = true
            /**
             * 采用异步加载SDK
             */
            asyncInitSDK = true
            /**
             * 是否是弱IM场景
             */
            reducedIM = false
            /**
             * 是否检查manifest 配置，调试阶段打开，调试通过之后请关掉
             */
            checkManifestConfig = false
            /**
             * 是否启用群消息已读功能，默认关闭
             */
            enableTeamMsgAck = true
            /**
             * 打开消息撤回未读数-1的开关
             */
            shouldConsiderRevokedMessageUnreadCount = true
            /**
             * 云信私有化配置项
             */
            configServerAddress(this)
            /**
             * 推送配置
             */
            mixPushConfig = buildMixPushConfig()
            loginCustomTag = "登录自定义字段"
        }
    }


    /**
     *云信私有化配置项
     */
    private fun configServerAddress(
        options: SDKOptions
    ) {
        options.serverConfig = null
        options.appKey = null
    }

    /**
     * 如果将新消息通知提醒托管给SDK完成，需要添加以下配置。
     */
    private fun initStatusBarNotificationConfig(options: SDKOptions) {
        val config = loadStatusBarNotificationConfig()
        options.statusBarNotificationConfig = config
    }

    private fun loadStatusBarNotificationConfig(): StatusBarNotificationConfig {
        val config = StatusBarNotificationConfig()
        config.notificationEntrance = ActivityNim::class.java
        return config
    }

    private fun buildMixPushConfig(): MixPushConfig { // 第三方推送配置
        val config = MixPushConfig()
        /**
         * 小米推送
         */
        config.xmAppId = "2882303761517502883"
        config.xmAppKey = "5671750254883"
        config.xmCertificateName = "DEMO_MI_PUSH"
        /**
         * 华为推送
         */
        config.hwAppId = "101420927"
        config.hwCertificateName = "DEMO_HW_PUSH"
        /**
         *  魅族推送
         */
        config.mzAppId = "111710"
        config.mzAppKey = "282bdd3a37ec4f898f47c5bbbf9d2369"
        config.mzCertificateName = "DEMO_MZ_PUSH"
        /**
         * fcm 推送，适用于海外用户，不使用fcm请不要配置
         */
        config.fcmCertificateName = "DEMO_FCM_PUSH"
        /**
         *  vivo推送
         */
        config.vivoCertificateName = "DEMO_VIVO_PUSH"
        /**
         * oppo推送
         */
        config.oppoAppId = "3477155"
        config.oppoAppKey = "6clw0ue1oZ8cCOogKg488o0os"
        config.oppoAppSercet = "e163705Bd018bABb3e2362C440A94673"
        config.oppoCertificateName = "DEMO_OPPO_PUSH"
        return config
    }


}