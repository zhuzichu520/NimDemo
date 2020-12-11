package com.chuzi.android.nim.ui.me.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/11 10:08 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.ACTIVITY_NIM_ME_CLEAN_SESSION)
class ActivityCleanSession : ActivityBase() {

    override fun getRoute(): String = RoutePath.Nim.FRAGMENT_NIM_ME_CLEAN_SESSION

}