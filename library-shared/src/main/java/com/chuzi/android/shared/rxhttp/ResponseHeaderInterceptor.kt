package com.chuzi.android.shared.rxhttp

import okhttp3.Interceptor
import okhttp3.Response

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/27 1:42 PM
 * since: v 1.0.0
 */
class ResponseHeaderInterceptor(
    private val name: String,
    private val value: String
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        return response.newBuilder().header(name, value).build()
    }

}