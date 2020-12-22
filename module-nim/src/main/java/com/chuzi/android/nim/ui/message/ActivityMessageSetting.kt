package com.chuzi.android.nim.ui.message

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

/** 聊天设置界面
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:25 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.ACTIVITY_NIM_MESSAGE_SETTING)
class ActivityMessageSetting : ActivityBase() {

    override fun getRoute(): String = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_SETTING

}