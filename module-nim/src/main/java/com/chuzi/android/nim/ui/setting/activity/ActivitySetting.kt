package com.chuzi.android.nim.ui.setting.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath.Nim.ACTIVITY_NIM_SETTING
import com.chuzi.android.shared.route.RoutePath.Nim.FRAGMENT_NIM_SETTING

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/10 2:29 PM
 * since: v 1.0.0
 */
@Route(path = ACTIVITY_NIM_SETTING)
class ActivitySetting : ActivityBase() {

    override fun getRoute(): String = FRAGMENT_NIM_SETTING

}