package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.emoji.EmojiManager
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.global.AppGlobal.context

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:26 PM
 * since: v 1.0.0
 */
data class ItemViewModelEmoji(
    val viewModel: BaseViewModel<*>,
    val index: Int
) : ItemViewModelBase(viewModel) {

    val text = EmojiManager.getDisplayText(index)

    val darawble = MutableLiveData<Any>().apply {
        value = EmojiManager.getDisplayDrawable(context, index)
    }

    val onClickEmojiCommand = createCommand {
        RxBus.post(EventUI.OnClickEmojiEvent(text.toString()))
    }

}