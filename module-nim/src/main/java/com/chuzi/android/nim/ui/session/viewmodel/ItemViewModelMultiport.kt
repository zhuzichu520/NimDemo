package com.chuzi.android.nim.ui.session.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.databinding.BindingCommand
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/4 11:42 AM
 * since: v 1.0.0
 */
class ItemViewModelMultiport(
    viewModel: BaseViewModel<*>
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData<String>()

    val isShown = MutableLiveData(false)

    val onClickCommand = MutableLiveData<BindingCommand<Any>>()

}