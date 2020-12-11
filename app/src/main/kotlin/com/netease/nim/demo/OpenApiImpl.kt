package com.netease.nim.demo

import com.chuzi.android.nim.api.OpenApi

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:10 PM
 * since: v 1.0.0
 */
class OpenApiImpl : OpenApi {

    override fun testApi(): String = "test"

    override fun logout() {

    }

}