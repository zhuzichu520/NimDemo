package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.shared.base.ItemViewModelBase
import java.util.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelSettingLanguage(
    viewModel: BaseViewModel<*>,
    locale: String?,
    localeLiveData: MutableLiveData<String>
) : ItemViewModelBase(viewModel) {

    val text = MutableLiveData<String>().apply {
        value = when (locale) {
            Locale.CHINA.language -> {
                "中文"
            }
            Locale.ENGLISH.language -> {
                "English"
            }
            else -> {
                "系统默认"
            }
        }
    }

    val isSelected: LiveData<Boolean> = Transformations.map(localeLiveData) {
        it == locale
    }

    val onClickCommand = createCommand {
        localeLiveData.value = locale
    }

}