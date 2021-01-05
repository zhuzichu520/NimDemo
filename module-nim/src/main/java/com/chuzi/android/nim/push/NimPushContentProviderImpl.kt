package com.chuzi.android.nim.push

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import org.json.JSONException
import org.json.JSONObject

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
class NimPushContentProviderImpl : NimPushContentProvider {

    override fun getPushContent(message: IMMessage): String? {
        return null
    }

    override fun getPushPayload(message: IMMessage): Map<String, Any> {
        return getPayload(message)
    }

    private fun getPayload(message: IMMessage): Map<String, Any> {
        val payload: HashMap<String, Any> = HashMap()
        val sessionType = message.sessionType.value
        payload["sessionType"] = sessionType
        var sessionId = ""
        if (message.sessionType == SessionTypeEnum.Team) {
            sessionId = message.sessionId
        } else if (message.sessionType == SessionTypeEnum.P2P) {
            sessionId = message.fromAccount
        }
        if (!TextUtils.isEmpty(sessionId)) {
            payload["sessionID"] = sessionId
        }
        //华为推送
        setHwField(payload, sessionType, sessionId)
        return payload
    }

    private fun setHwField(
        pushPayload: MutableMap<String, Any>,
        sessionType: Int,
        sessionId: String
    ) {
        //hwField
        val hwIntent = Intent(Intent.ACTION_VIEW)
        val intentStr = String.format(
            "pushscheme://com.huawei.codelabpush/deeplink?sessionID=%s&sessionType=%s",
            sessionId, sessionType
        )
        hwIntent.data = Uri.parse(intentStr)
        hwIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intentUri = hwIntent.toUri(Intent.URI_INTENT_SCHEME)
        //点击事件的内容
        val clickAction = JSONObject()
        //通知的内容
        val notification = JSONObject()
        try {
            clickAction.putOpt("type", 1)
                .putOpt("intent", intentUri)
            notification.putOpt("click_action", clickAction)
            pushPayload["hwField"] = notification
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }
}