package com.chuzi.android.nim.ui.me.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelMeGroup(
    viewModel: BaseViewModel<*>,
    @StringRes textId: Int,
    @DrawableRes iconId: Int,
    onClickFunc: (() -> Unit)? = null
) : ItemViewModelBase(viewModel) {

    val iconId = MutableLiveData(iconId)

    val textId = MutableLiveData(textId)

    val onClickCommand = createCommand {
        onClickFunc?.invoke()
    }

}