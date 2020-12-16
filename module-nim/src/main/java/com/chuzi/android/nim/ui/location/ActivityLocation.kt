package com.chuzi.android.nim.ui.location

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.tool.immersiveDark
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/8 3:19 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Map.ACTIVITY_MAP_LOCATION)
class ActivityLocation : ActivityBase() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersiveDark(darkMode = false)
    }

    override fun getRoute(): String = RoutePath.Map.FRAGMENT_MAP_LOCATION

}