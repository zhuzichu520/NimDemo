package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.global.AppGlobal
import com.chuzi.android.shared.storage.AppStorage
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc 设置ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelSettingTheme : ViewModelBase<ArgDefault>() {

    val uiMode = MutableLiveData(AppStorage.uiMode)

    /**
     * 所有数据
     */
    val items = MutableLiveData<List<Any>>()

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
        map<ItemViewModelSettingTheme>(BR.item, R.layout.nim_item_setting_theme)
    }

}
