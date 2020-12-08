package com.chuzi.android.nim.api

import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 3:51 PM
 * since: v 1.0.0
 * @param router 路由地址
 */
enum class EnumPage(
    val router: String
) {
    /**
     * 会话界面
     */
    SESSION(RoutePath.Nim.FRAGMENT_NIM_SESSION),

    /**
     * 联系人界面
     */
    CONTRACT(RoutePath.Nim.FRAGMENT_NIM_CONTRACT),

    /**
     * 设置界面
     */
    SETTING(RoutePath.Nim.FRAGMENT_NIM_SETTING)
}