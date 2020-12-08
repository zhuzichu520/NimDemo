package com.chuzi.android.nim.core.provider

import android.content.Context
import android.graphics.Bitmap
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.uinfo.UserInfoProvider
import com.netease.nimlib.sdk.uinfo.model.UserInfo

/**
 * desc 用户信息提供 影响通知消息中的用户展示
 * author: 朱子楚
 * time: 2020/12/4 4:18 PM
 * since: v 1.0.0
 */
class NimUserInfoProvider(
    val context: Context
) : UserInfoProvider {

    override fun getUserInfo(account: String?): UserInfo? {
        return null
    }

    override fun getAvatarForMessageNotifier(
        sessionType: SessionTypeEnum?,
        sessionId: String?
    ): Bitmap? {
        return null
    }

    override fun getDisplayNameForMessageNotifier(
        account: String?,
        sessionId: String?,
        sessionType: SessionTypeEnum?
    ): String? {
        return null
    }

}