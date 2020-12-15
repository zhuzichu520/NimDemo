package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.nim.core.attachment.AttachmentSticker
import com.chuzi.android.nim.emoji.StickerManager
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 自定义消息-贴图消息
 * author: 朱子楚
 * time: 2020/4/22 8:28 PM
 * since: v 1.0.0
 */
class ItemViewModelMessageSticker(
    viewModel: ViewModelMessage,
    message: IMMessage
) : ItemViewModelMessageBase(viewModel, message) {

    val sticker = MutableLiveData<Any>().apply {
        (message.attachment as? AttachmentSticker)?.let {
            value = StickerManager.getStickerUri(it.catalog, it.chartlet)
        }
    }
}