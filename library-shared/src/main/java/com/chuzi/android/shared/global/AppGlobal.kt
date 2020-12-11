package com.chuzi.android.shared.global

import android.app.Application
import android.content.Context
import android.util.Log
import coil.ImageLoader
import coil.util.DebugLogger
import com.chuzi.android.libs.tool.decodeBase64
import com.chuzi.android.libs.tool.json2Object
import com.chuzi.android.mvvm.base.IApplication
import com.chuzi.android.widget.log.lumberjack.FileLoggingSetup
import com.chuzi.android.widget.log.lumberjack.FileLoggingTree
import com.chuzi.android.widget.log.lumberjack.L
import com.chuzi.android.shared.BuildConfig
import com.chuzi.android.shared.module.Modules
import com.chuzi.android.shared.rxhttp.ResponseHeaderInterceptor
import com.tencent.mmkv.MMKV
import okhttp3.Cache
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import timber.log.ConsoleTree
import java.io.File

object AppGlobal {

    private lateinit var application: Application

    lateinit var imageLoader: ImageLoader

    val context: Context by lazy {
        application.applicationContext
    }

    fun init(application: Application): AppGlobal {
        AppGlobal.application = application
        initImageLoader()
        CacheGlobal.initDir()
        MMKV.initialize(CacheGlobal.getMmkvCacheDir())
        L.plant(ConsoleTree())
        L.plant(FileLoggingTree(FileLoggingSetup(context).withFolder(CacheGlobal.getLogCacheDir())))
        json2Object(decodeBase64(BuildConfig.MODULE_JSON), Modules::class.java)?.data?.forEach {
            try {
                val clz = Class.forName(it.application)
                val obj = clz.newInstance()
                if (obj is IApplication) {
                    L.tag("zzc").i { "初始化模块Application:".plus(it.application) }
                    obj.init(application)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return this
    }

    private fun initImageLoader() {
        imageLoader = ImageLoader.Builder(context)
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
    }

}