package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc 设置ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelSetting : ViewModelBase<ArgDefault>() {

    val onClickLogoutEvent = SingleLiveEvent<Unit>()

    /**
     * 所有数据
     */
    val items = MutableLiveData<List<Any>>()

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
        map<ItemViewModelSettingGroup>(BR.item, R.layout.nim_item_setting_group)
        map<ItemViewModelSettingSwitch>(BR.item, R.layout.nim_item_setting_switch)
        map<ItemViewModelSettingLogout>(BR.item, R.layout.nim_item_setting_logout)
    }

    /**
     * 初始化数据
     */
    override fun initData() {
        super.initData()
        val data = ArrayList<Any>()
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelSettingGroup(
                this,
                R.string.setting_account_safe
            )
        )
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelSettingGroup(
                this,
                R.string.setting_font_size
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_FONT)
            }
        )
        data.add(
            ItemViewModelSettingGroup(
                this,
                R.string.setting_new_session
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_NOTIFICATION)
            }
        )
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelSettingGroup(
                this,
                R.string.settings_language_title
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_LANGUAGE)
            }
        )
        data.add(
            ItemViewModelSettingGroup(
                this,
                R.string.settings_theme_title
            ) {
                navigate(RoutePath.Nim.FRAGMENT_NIM_SETTING_THEME)
            }
        )
        data.add(
            ItemViewModelSettingSwitch(
                this,
                R.string.settings_one_button_gray,
                AppFactorySDK.getGrayImageLiveData()
            ) {
                val isGray = AppFactorySDK.isGrayImage.value != true
                AppStorage.isGray = isGray
                AppFactorySDK.isGrayImage.value = isGray
            }
        )
        data.add(ItemViewModelLine(this))
        data.add(ItemViewModelSettingLogout(this) {
            onClickLogoutEvent.call()
        })
        items.value = data
    }
}
