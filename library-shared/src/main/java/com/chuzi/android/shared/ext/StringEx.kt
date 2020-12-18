package com.chuzi.android.shared.ext

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * desc
 * author: 朱子楚
 * time: 2020/12/14 5:08 PM
 * since: v 1.0.0
 */

fun Any?.toString2(): String {
    return this?.toString() ?: ""
}

/**
 * 复制到剪贴板
 */
fun String?.copyClipboard(context: Context) {
    // 获取系统剪贴板
    val clipboard: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
    val clipData = ClipData.newPlainText(null, this)
    // 把数据集设置（复制）到剪贴板
    clipboard.setPrimaryClip(clipData)
}