package com.chuzi.android.nim.entity.arg

import com.chuzi.android.mvvm.base.BaseArg
import com.chuzi.android.nim.emoji.StickerCategory


/**
 * desc 消息界面参数
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
data class ArgSticker(
    val stickerCategory: StickerCategory,
) : BaseArg()