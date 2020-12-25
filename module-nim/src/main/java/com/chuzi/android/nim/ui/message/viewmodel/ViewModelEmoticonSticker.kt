package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.entity.arg.ArgSticker
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/15 6:06 PM
 * since: v 1.0.0
 */
class ViewModelEmoticonSticker : ViewModelBase<ArgSticker>() {

    val items = MutableLiveData<List<Any>>()

    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSticker>(BR.item, R.layout.nim_item_sticker)
    }

}