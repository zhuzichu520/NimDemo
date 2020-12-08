package com.netease.nim.demo

import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

class ActivityMain : ActivityBase() {
    override fun getRoute(): String = RoutePath.Login.FRAGMENT_LOGIN_MAIN
}