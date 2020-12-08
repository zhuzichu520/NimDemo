package com.chuzi.android.nim.ext

import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.AuthServiceObserver
import com.netease.nimlib.sdk.friend.FriendServiceObserve
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.MsgServiceObserve
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.uinfo.UserService
import com.netease.nimlib.sdk.uinfo.UserServiceObserve

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/4 7:51 PM
 * since: v 1.0.0
 */

/**
 * 获取MsgService
 */
fun msgService(): MsgService {
    return NIMClient.getService(MsgService::class.java)
}

/**
 * 获取MsgService
 */
fun teamService(): TeamService {
    return NIMClient.getService(TeamService::class.java)
}

fun authService(): AuthService {
    return NIMClient.getService(AuthService::class.java)
}

fun userService(): UserService {
    return NIMClient.getService(UserService::class.java)
}

fun authServiceObserver(): AuthServiceObserver {
    return NIMClient.getService(AuthServiceObserver::class.java)
}

fun msgServiceObserve(): MsgServiceObserve {
    return NIMClient.getService(MsgServiceObserve::class.java)
}

fun userServiceObserve(): UserServiceObserve {
    return NIMClient.getService(UserServiceObserve::class.java)
}

fun friendServiceObserve(): FriendServiceObserve {
    return NIMClient.getService(FriendServiceObserve::class.java)
}