package com.chuzi.android.nim.core.attachment

import com.alibaba.fastjson.JSONObject
import com.chuzi.android.nim.core.attachment.NimAttachParser.Companion.packData
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment


/**
 * desc 附件基类
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
abstract class NimAttachment internal constructor(val type: Int) : MsgAttachment {

    fun fromJson(data: JSONObject) {
        parseData(data)
    }

    override fun toJson(send: Boolean): String {
        return packData(type, packData())
    }

    protected abstract fun parseData(data: JSONObject)

    protected abstract fun packData(): JSONObject

}