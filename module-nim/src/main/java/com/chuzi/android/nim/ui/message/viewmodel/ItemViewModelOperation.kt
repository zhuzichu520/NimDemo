package com.chuzi.android.nim.ui.message.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.bus.RxBus

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 3:59 PM
 * since: v 1.0.0
 */
data class ItemViewModelOperation(
    private val viewModel: BaseViewModel<*>,
    @StringRes private val titleId: Int,
    @DrawableRes private val iconId: Int
) : ItemViewModelBase(viewModel) {

    val title = MutableLiveData(titleId)

    val icon = MutableLiveData(iconId)

    val onClickCommand = createCommand {
        RxBus.post(EventUI.OnClickOperationEvent(titleId))
    }

}