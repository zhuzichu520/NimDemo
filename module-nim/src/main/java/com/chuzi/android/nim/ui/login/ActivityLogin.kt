package com.chuzi.android.nim.ui.login

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Login.ACTIVITY_LOGIN_MAIN)
class ActivityLogin : ActivityBase() {

    override fun getRoute(): String = RoutePath.Login.FRAGMENT_LOGIN_MAIN

}