package com.chuzi.android.nim.push

import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */
interface NimPushContentProvider {
    /**
     * 在消息发出去之前，回调此方法，用户需实现自定义的推送文案
     *
     * @param message
     */
    fun getPushContent(message: IMMessage): String?

    /**
     * 在消息发出去之前，回调此方法，用户需实现自定义的推送payload，它可以被消息接受者在通知栏点击之后得到
     *
     * @param message
     */
    fun getPushPayload(message: IMMessage): Map<String, Any>

}