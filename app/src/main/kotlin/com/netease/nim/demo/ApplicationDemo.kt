package com.netease.nim.demo

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.multidex.MultiDex
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.DebugLogger
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.shared.global.CacheGlobal
import com.chuzi.android.shared.rxhttp.ResponseHeaderInterceptor
import com.chuzi.android.shared.skin.SkinManager
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.io.File

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/3 5:08 PM
 * since: v 1.0.0
 */

class ApplicationDemo : Application(), ImageLoaderFactory {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        AppFactorySDK.init(this, OpenApiImpl())
    }

    /**
     * 初始化ImageLoader
     */
    override fun newImageLoader(): ImageLoader = ImageLoader.Builder(this)
        .availableMemoryPercentage(0.25)
        .okHttpClient {
            val cache = Cache(File(CacheGlobal.getCoilCacheDir()), Long.MAX_VALUE)
            val cacheControlInterceptor =
                ResponseHeaderInterceptor("Cache-Control", "max-age=31536000,public")
            val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }
            OkHttpClient.Builder()
                .cache(cache)
                .dispatcher(dispatcher)
                .addNetworkInterceptor(cacheControlInterceptor)
                .build()
        }
        .apply {
            if (BuildConfig.DEBUG) {
                logger(DebugLogger(Log.VERBOSE))
            }
        }
        .build()

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        SkinManager.applyConfigurationChanged(newConfig)
    }

}
