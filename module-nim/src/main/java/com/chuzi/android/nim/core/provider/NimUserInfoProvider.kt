package com.chuzi.android.nim.core.provider

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.content.ContextCompat
import com.chuzi.android.nim.R
import com.chuzi.android.nim.tools.ToolUserInfo
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
        return ToolUserInfo.getUserInfo(account)
    }

    override fun getAvatarForMessageNotifier(
        sessionType: SessionTypeEnum?,
        sessionId: String?
    ): Bitmap? {
        return (ContextCompat.getDrawable(
            context,
            R.mipmap.nim_avatar_default
        ) as? BitmapDrawable)?.bitmap
    }

    override fun getDisplayNameForMessageNotifier(
        account: String?,
        sessionId: String?,
        sessionType: SessionTypeEnum?
    ): String? {
        return ToolUserInfo.getUserName(account)
    }

}