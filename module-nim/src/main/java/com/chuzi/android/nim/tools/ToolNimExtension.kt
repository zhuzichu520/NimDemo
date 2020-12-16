package com.chuzi.android.nim.tools

import com.chuzi.android.libs.tool.toCast
import com.chuzi.android.nim.ext.msgService
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/10 3:18 PM
 * since: v 1.0.0
 */
object ToolNimExtension {

    /**
     * 会话置顶
     */
    private const val SESSION_STICK = "SESSION_STICK"

    /**
     * 会话草稿
     */
    private const val SESSION_DRAFT = "SESSION_DRAFT"

    fun addStickyTag(contact: RecentContact) {
        contact.put(SESSION_STICK, System.currentTimeMillis())
    }

    fun removeStickTag(contact: RecentContact) {
        contact.put(SESSION_STICK, null)
    }

    fun isStickyTagSet(contact: RecentContact): Boolean {
        return contact.isValue(SESSION_STICK)
    }

    fun getStickyLong(contact: RecentContact): Long {
        return (contact.get(SESSION_STICK) ?: Long.MIN_VALUE).toCast()
    }

    fun addDraft(contact: RecentContact, draft: String) {
        contact.put(SESSION_DRAFT, draft)
    }

    fun getDraft(contact: RecentContact): String? {
        val obj = contact.get(SESSION_DRAFT) ?: return null
        return obj.toCast()
    }

    fun removeDraft(contact: RecentContact) {
        contact.put(SESSION_DRAFT, null)
    }

    private fun RecentContact.put(key: String, obj: Any?) {
        val extension = this.extension ?: mutableMapOf()
        extension[key] = obj
        this.extension = extension
        msgService().updateRecentAndNotify(this)
    }

    private fun RecentContact.isValue(key: String): Boolean {
        val extension = this.extension ?: return false
        extension[key] ?: return false
        return true
    }

    private fun RecentContact.get(key: String): Any? {
        val extension = this.extension ?: return null
        return extension[key]
    }

}