package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 5:40 PM
 * since: v 1.0.0
 */
class ItemViewModelFriendIndex(
    viewModel: BaseViewModel<*>,
    val letter: String
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<String>().apply {
        value = letter
    }

}