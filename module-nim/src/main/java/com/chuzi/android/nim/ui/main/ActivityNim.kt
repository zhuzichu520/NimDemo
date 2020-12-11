package com.chuzi.android.nim.ui.main

import android.content.Intent
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.Mvvm
import com.chuzi.android.nim.ext.authService
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/9 11:41 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.ACTIVITY_NIM_MAIN)
class ActivityNim : ActivityBase() {

    override fun getRoute(): String = RoutePath.Nim.FRAGMENT_NIM_MAIN

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val extras = intent?.extras ?: return
        val arg = extras.getSerializable(Mvvm.KEY_ARG) as? ArgNim ?: return
        when (arg.mainType) {
            EnumNimType.LOGOUT -> {
                navigate(RoutePath.Login.ACTIVITY_LOGIN_MAIN, isPop = true)
            }
            else -> {

            }
        }
    }

}