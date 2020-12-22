package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
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

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelEmoticonTab>(BR.item, R.layout.nim_item_emoticon_tab)
    }

    val items = MutableLiveData<List<Any>>()

    fun updateTabs() {
        items.value?.forEachIndexed { index, any ->
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