package com.chuzi.android.nim.core.attachment

import com.alibaba.fastjson.JSONObject
import com.chuzi.android.libs.tool.getFileNameNoEx

/**
 * desc 贴图附件
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
class AttachmentSticker() : NimAttachment(NimAttachmentType.Sticker) {

    lateinit var catalog: String

    lateinit var chartlet: String

    constructor(catalog: String, emotion: String) : this() {
        this.catalog = catalog
        chartlet = getFileNameNoEx(emotion).toString()
    }

    override fun parseData(data: JSONObject) {
        catalog = data.getString(KEY_CATALOG)
        chartlet = data.getString(KEY_CHARTLET)
    }

    override fun packData(): JSONObject {
        val data = JSONObject()
        data[KEY_CATALOG] = catalog
        data[KEY_CHARTLET] = chartlet
        return data
    }

    companion object {

        private const val KEY_CATALOG = "catalog"

        private const val KEY_CHARTLET = "chartlet"

    }
}