package com.chuzi.android.nim.push

import com.chuzi.android.widget.log.lumberjack.L
import com.huawei.hms.push.RemoteMessage
import com.netease.nimlib.sdk.mixpush.HWPushMessageService


/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
class NimHwPushMessageService : HWPushMessageService() {

    companion object {
        private const val TAG = "NimHwPushMessageService"
    }

    override fun onNewToken(token: String) {
        L.tag(TAG).i { " onNewToken, token=$token" }
    }

    /**
     * 透传消息， 需要用户自己弹出通知
     *
     * @param remoteMessage
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        L.tag(TAG).i { " onMessageReceived" }
    }

    override fun onMessageSent(s: String) {
        L.tag(TAG).i { " onMessageSent" }
    }

    override fun onDeletedMessages() {
        L.tag(TAG).i { " onDeletedMessages" }
    }

    override fun onSendError(var1: String, var2: Exception) {
        L.tag(TAG).i(var2) { " onSendError, $var1" }
    }

}
