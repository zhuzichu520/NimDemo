package com.chuzi.android.nim.ui.message.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.emoji.StickerManager
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */
class ViewModelMessageEmoticon : ViewModelBase<ArgDefault>() {


    val tabIndex = MutableLiveData<Int>()

    val pageItems = ObservableArrayList<Any>().also {
        it.add(ItemViewModelEmojiPage(this))
        val categories = StickerManager.categories
        categories.forEach { item ->
            it.add(ItemViewModelStickerPage(this, item))
        }
    }

    val pageItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmojiPage>(BR.item, R.layout.nim_item_emoticon_page_emoji)
        map<ItemViewModelStickerPage>(BR.item, R.layout.nim_item_emoticon_page_sticker)
    }

    val tabItemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmoticonTab>(BR.item, R.layout.nim_item_emoticon_tab)
    }

    val tabItems = ObservableArrayList<Any>().also {
        var i = 0
        it.add(
            ItemViewModelEmoticonTab(
                this,
                R.mipmap.nim_ic_emoji_normal,
                R.mipmap.nim_ic_emoji_pressed,
                i++
            )
        )
        val categories = StickerManager.categories
        categories.forEach { item ->
            it.add(
                ItemViewModelEmoticonTab(
                    this,
                    item.normalUri,
                    item.pressedlUri,
                    i++
                )
            )
        }
    }

    fun updateTabs() {
        tabItems.forEachIndexed { index, any ->
            (any as? ItemViewModelEmoticonTab)?.let {
                if (index == tabIndex.value) {
                    it.icon.value = it.pressed
                } else {
                    it.icon.value = it.normal
                }
            }
        }
    }


}