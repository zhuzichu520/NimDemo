package com.chuzi.android.nim.core.attachment

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.netease.nimlib.sdk.msg.attachment.MsgAttachment
import com.netease.nimlib.sdk.msg.attachment.MsgAttachmentParser

/**
 * desc 附件解析器
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
class NimAttachParser : MsgAttachmentParser {

    /**
     * 解析json
     */
    override fun parse(json: String): MsgAttachment {
        val obj = JSON.parseObject(json)
        val type = obj.getInteger(KEY_TYPE)
        val data = obj.getJSONObject(KEY_DATA)
        val attachment: NimAttachment = when (type) {
            NimAttachmentType.Sticker -> AttachmentSticker()
            else -> AttachmentDefault()
        }
        attachment.fromJson(data)
        return attachment
    }

    companion object {

        private const val KEY_TYPE = "type"

        private const val KEY_DATA = "data"

        fun packData(type: Int, data: JSONObject): String {
            val obj = JSONObject()
            obj[KEY_TYPE] = type
            obj[KEY_DATA] = data
            return obj.toJSONString()
        }
    }

}