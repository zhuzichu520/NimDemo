package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.emoji.StickerItem
import com.chuzi.android.nim.emoji.StickerManager
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.bus.RxBus

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 2:50 PM
 * since: v 1.0.0
 */
data class ItemViewModelSticker(
    val viewModel: BaseViewModel<*>,
    val stickerItem: StickerItem
) : ItemViewModelBase(viewModel) {

    val icon = MutableLiveData<Any>().apply {
        value = StickerManager.getStickerUri(stickerItem.category, stickerItem.name)
    }

    val onClickStickerCommand = createCommand {
        RxBus.post(EventUI.OnClickStickerEvent(stickerItem))
    }

}