package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.shared.base.ItemViewModelBase

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelSettingTheme(
    viewModel: BaseViewModel<*>,
    mode: Int,
    uiMode: MutableLiveData<Int>
) : ItemViewModelBase(viewModel) {

    val textId = MutableLiveData<Int>().apply {
        value = when (mode) {
            MODE_NIGHT_NO -> {
                R.string.settings_theme_light
            }
            MODE_NIGHT_YES -> {
                R.string.settings_theme_dark
            }
            else -> {
                R.string.settings_theme_system
            }
        }
    }

    val isSelected: LiveData<Boolean> = Transformations.map(uiMode) {
        it == mode
    }

    val onClickCommand = createCommand {
        uiMode.value = mode
    }

}