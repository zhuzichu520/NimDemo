package com.chuzi.android.nim.core.error

/**
 * desc IM自定义异常
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimThrowable(val code: Int, override val message: String) : RuntimeException()