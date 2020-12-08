package com.chuzi.android.nim.core.attachment

import com.alibaba.fastjson.JSONObject

/**
 * desc 默认附件
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
class AttachmentDefault : NimAttachment(0) {

    lateinit var content: String

    override fun parseData(data: JSONObject) {
        content = data.toJSONString()
    }

    override fun packData(): JSONObject {
        return JSONObject.parseObject(content)
    }

}