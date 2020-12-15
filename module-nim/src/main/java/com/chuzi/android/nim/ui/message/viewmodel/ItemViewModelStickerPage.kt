package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.emoji.StickerCategory
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:06 PM
 * since: v 1.0.0
 */
data class ItemViewModelStickerPage(
    val viewModel: BaseViewModel<*>,
    val stickerCategory: StickerCategory
) : ItemViewModelBase(viewModel) {

    val items = MutableLiveData<List<Any>>().apply {
        val stickers = stickerCategory.stickers
        value = stickers?.map {
            ItemViewModelSticker(viewModel, it)
        }
    }

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSticker>(BR.item, R.layout.nim_item_sticker)
    }

}