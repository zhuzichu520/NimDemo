package com.chuzi.android.nim.ui.contract.activity

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.shared.base.ActivityBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 群组
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.ACTIVITY_NIM_CONTRACT_TEAMGROUPS)
class ActivityTeamGroups : ActivityBase() {

    override fun getRoute(): String = RoutePath.Nim.FRAGMENT_NIM_CONTRACT_TEAMGROUPS

}