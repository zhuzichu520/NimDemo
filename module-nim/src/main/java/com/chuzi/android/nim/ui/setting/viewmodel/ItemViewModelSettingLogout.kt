package com.chuzi.android.nim.ui.setting.viewmodel

import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelSettingLogout(
    viewModel: BaseViewModel<*>,
    onClickFunc: (() -> Unit)? = null
) : ItemViewModelBase(viewModel) {

    val onClickCommand = createCommand {
        onClickFunc?.invoke()
    }

}