package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.emoji.EmojiManager
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:06 PM
 * since: v 1.0.0
 */
class ItemViewModelEmojiPage(
    viewModel: BaseViewModel<*>
) : ItemViewModelBase(viewModel) {

    val items = MutableLiveData<List<Any>>().apply {
        val list = mutableListOf<ItemViewModelEmoji>()
        for (i in 0 until EmojiManager.displayCount) {
            list.add(ItemViewModelEmoji(viewModel, i))
        }
        value = list
    }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmoji>(BR.item, R.layout.nim_item_emoji)
    }

    val onClickCommand = createCommand {
        RxBus.post(EventUI.OnClickEmojiDeleteEvent())
    }

}