package com.chuzi.android.nim.push

import android.app.Activity
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.chuzi.android.nim.core.config.NimConfigSDKOption
import com.chuzi.android.shared.global.AppGlobal.context
import com.netease.nimlib.sdk.NimIntent
import com.netease.nimlib.sdk.StatusBarNotificationConfig
import com.netease.nimlib.sdk.mixpush.MixPushMessageHandler
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
class NimMixPushMessageHandler : MixPushMessageHandler {

    companion object {
        const val PAYLOAD_SESSION_ID = "sessionID"
        const val PAYLOAD_SESSION_TYPE = "sessionType"
    }

    // 对于华为推送，这个方法并不能保证一定会回调
    private fun initLaunchComponent(context: Context): ComponentName? {
        val config: StatusBarNotificationConfig =
            NimConfigSDKOption.getStatusBarNotificationConfig()
        val entrance: Class<out Activity?>? = config.notificationEntrance
        return entrance?.let { ComponentName(context, it) }
            ?: context.packageManager.getLaunchIntentForPackage(context.packageName)?.component
    }

    override fun onNotificationClicked(
        context: Context?,
        payload: Map<String, String>?
    ): Boolean {
        val sessionId: String? = payload?.get(PAYLOAD_SESSION_ID)
        val type: String? = payload?.get(PAYLOAD_SESSION_TYPE)
        //
        return if (sessionId != null && type != null) {
            val typeValue = Integer.valueOf(type)
            val imMessages: ArrayList<IMMessage> = ArrayList()
            val imMessage = MessageBuilder.createEmptyMessage(
                sessionId,
                SessionTypeEnum.typeOfValue(typeValue),
                0
            )
            imMessages.add(imMessage)
            context?.let {
                val notifyIntent = Intent()
                notifyIntent.component = initLaunchComponent(it)
                notifyIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                notifyIntent.action = Intent.ACTION_VIEW
                notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // 必须
                notifyIntent.putExtra(NimIntent.EXTRA_NOTIFY_CONTENT, imMessages)
                it.startActivity(notifyIntent)
            }
            true
        } else {
            false
        }
    }

    // 将音视频通知 Notification 缓存，清除所有通知后再次弹出 Notification，避免清除之后找不到打开正在进行音视频通话界面的入口
    override fun cleanMixPushNotifications(pushType: Int): Boolean {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
        return true
    }

}