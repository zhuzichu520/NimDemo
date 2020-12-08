package com.chuzi.android.shared.rxhttp

import android.app.Application
import com.chuzi.android.shared.BuildConfig
import com.chuzi.android.shared.global.CacheGlobal
import com.chuzi.android.shared.storage.AppStorage
import okhttp3.OkHttpClient
import rxhttp.RxHttp
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.cahce.CacheMode
import rxhttp.wrapper.cookie.CookieStore
import rxhttp.wrapper.ssl.HttpsUtils
import java.io.File
import java.util.concurrent.TimeUnit


/**
 * desc
 * author: 朱子楚
 * time: 2020/9/27 11:06 AM
 * since: v 1.0.0
 */
class RxHttpManager(
    val application: Application
) {

    init {
        val sslParams = HttpsUtils.getSslSocketFactory()
        val client: OkHttpClient = OkHttpClient.Builder()
            .cookieJar(CookieStore(File(CacheGlobal.getCookieCacheDir())))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier { _, _ -> true } //忽略host验证
            //.followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            //.addInterceptor(new RedirectInterceptor())
            //.addInterceptor(new TokenInterceptor())
            .build()
        //RxHttp初始化，自定义OkHttpClient对象，非必须
        RxHttp.init(client, BuildConfig.DEBUG)
        //设置缓存策略，非必须
        RxHttpPlugins.setCache(
            File(CacheGlobal.getHttpCacheDir()),
            100 * 1024 * 1024,
            CacheMode.REQUEST_NETWORK_FAILED_READ_CACHE,
            15 * 24 * 60 * 60 * 1000
        )
        RxHttpPlugins.setExcludeCacheKeys("time") //设置一些key，不参与cacheKey的组拼
        //设置数据解密/解码器，非必须
        //RxHttp.setResultDecoder(s -> s)

        //设置全局的转换器，非必须
        //RxHttp.setConverter(FastJsonConverter.create())

        //设置公共参数，非必须

        //设置数据解密/解码器，非必须
        //RxHttp.setResultDecoder(s -> s)

        //设置全局的转换器，非必须
        //RxHttp.setConverter(FastJsonConverter.create())

        //设置公共参数，非必须
        RxHttp.setOnParamAssembly { param ->
            val method = param.method
            if (method.isGet) {

            }

            if (method.isPost) {

            }
            AppStorage.token?.let {
                val auth = if (it.startsWith("Basic")) it else "token $it"
                param.addHeader("Authorization", auth) //添加公共请求头
            }
            param
        }
    }


}