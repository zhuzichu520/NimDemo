package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 3:28 PM
 * since: v 1.0.0
 */
data class ItemViewModelEmoticonTab(
    val viewModel: ViewModelMessageEmoticon,
    val normal: Any,
    val pressed: Any,
    val index: Int
) : ItemViewModelBase(viewModel) {

    val icon = MutableLiveData(normal)

    val onClickTabCommand = createCommand {
        viewModel.tabIndex.value = index
    }

}