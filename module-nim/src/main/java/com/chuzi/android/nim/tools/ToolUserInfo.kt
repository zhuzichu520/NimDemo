package com.chuzi.android.nim.tools

import com.chuzi.android.nim.ext.userService
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.friend.FriendService
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc 用户资料工具类
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
object ToolUserInfo {

    /**
     * 获取NIM托管用户信息，如果本地没有就会直接去服务端拉去用户资料，然后触发observeUserInfoUpdate回调
     */
    fun getUserInfo(account: String?): NimUserInfo? {
        return userService().getUserInfo(account)
    }

    /**
     * 获取用户名
     */
    fun getUserName(account: String?): String? {
        return getUserInfo(account)?.name
    }

    /**
     * 获取别名
     */
    fun getAlias(account: String?): String? {
        val friend = NIMClient.getService(FriendService::class.java).getFriendByAccount(account)
            ?: return null
        return friend.alias
    }

    /**
     * 先获取别名，没有别名就获取用户名
     */
    fun getUserDisplayName(account: String?): String? {
        val name = getUserInfo(account)?.name
        val alias: String? = getAlias(account)
        if (alias.isNullOrEmpty())
            return name
        return alias
    }

}