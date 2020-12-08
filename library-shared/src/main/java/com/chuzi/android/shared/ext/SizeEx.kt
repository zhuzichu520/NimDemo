package com.chuzi.android.shared.ext

import android.content.Context
import com.chuzi.android.shared.global.AppGlobal

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/4 10:03 AM
 * since: v 1.0.0
 */

fun Float.dp2px(context: Context? = null): Int {
    return com.chuzi.android.libs.tool.dp2px((context ?: AppGlobal.context), this)
}