package com.netease.nim.demo

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.multidex.MultiDex
import com.chuzi.android.nim.api.AppFactorySDK

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */

class ApplicationDemo : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppFactorySDK.init(this, OpenApiImpl())
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        AppFactorySDK.applyConfigurationChanged(newConfig)
    }

}
