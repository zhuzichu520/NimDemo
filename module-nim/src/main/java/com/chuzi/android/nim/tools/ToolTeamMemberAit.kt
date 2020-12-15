package com.chuzi.android.nim.tools

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.shared.storage.AppStorage.account
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import java.util.regex.Pattern
import kotlin.collections.ArrayList

object ToolTeamMemberAit {
    private const val KEY_AIT = "ait"
    fun getAitAlertString(content: String): String {
        return "[有人@你] $content"
    }

    fun replaceAitForeground(value: String?, mSpannableString: SpannableString) {
        if (TextUtils.isEmpty(value) || TextUtils.isEmpty(mSpannableString)) {
            return
        }
        val pattern = Pattern.compile("(\\[有人@你\\])")
        val matcher = pattern.matcher(value)
        while (matcher.find()) {
            val start = matcher.start()
            if (start != 0) {
                continue
            }
            val end = matcher.end()
            mSpannableString.setSpan(
                ForegroundColorSpan(Color.RED),
                start,
                end,
                Spannable.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }
    }

    fun isAitMessage(message: IMMessage?): Boolean {
        if (message == null || message.sessionType != SessionTypeEnum.Team) {
            return false
        }
        val option = message.memberPushOption
        return option != null && option.isForcePush &&
                (option.forcePushList == null || option.forcePushList.contains(account))
    }

    fun hasAitExtension(recentContact: RecentContact?): Boolean {
        if (recentContact == null || recentContact.sessionType != SessionTypeEnum.Team) {
            return false
        }
        val ext = recentContact.extension ?: return false
        val mid = ext[KEY_AIT] as List<String>?
        return mid != null && !mid.isEmpty()
    }

    fun clearRecentContactAited(recentContact: RecentContact?) {
        if (recentContact == null || recentContact.sessionType != SessionTypeEnum.Team) {
            return
        }
        val exts = recentContact.extension
        if (exts != null) {
            exts[KEY_AIT] = null
        }
        recentContact.extension = exts
        msgService().updateRecent(recentContact)
    }

    fun buildAitExtensionByMessage(extention: MutableMap<String, Any>, message: IMMessage?) {
        if (message == null || message.sessionType != SessionTypeEnum.Team) {
            return
        }
        val mid = (extention[KEY_AIT] as? ArrayList<String>) ?: mutableListOf()
        if (!mid.contains(message.uuid)) {
            mid.add(message.uuid)
        }
        extention[KEY_AIT] = mid
    }

    fun setRecentContactAited(recentContact: RecentContact, messages: Set<IMMessage>) {
        if (recentContact.sessionType != SessionTypeEnum.Team) {
            return
        }
        var extension = recentContact.extension ?: mutableMapOf()
        val iterator = messages.iterator()
        while (iterator.hasNext()) {
            val msg = iterator.next()
            buildAitExtensionByMessage(extension, msg)
        }
        recentContact.extension = extension
        msgService().updateRecent(recentContact)
    }
}