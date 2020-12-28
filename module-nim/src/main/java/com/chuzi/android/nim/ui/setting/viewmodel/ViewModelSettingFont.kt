package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.storage.AppStorage

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ViewModelSettingFont : ViewModelBase<ArgDefault>() {

    val progress = MutableLiveData(AppStorage.fontLevel)

    val textSize: LiveData<Int> = AppFactorySDK.getFontSizeLiveData(18f)
}