package com.chuzi.android.nim.ui.setting.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.core.config.NimConfigSDKOption
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.storage.UserStorage
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ViewModelSettingNotification : ViewModelBase<ArgDefault>() {

    /**
     * 声音
     */
    private val ringChecked = MutableLiveData(UserStorage.ring)

    /**
     * 振动
     */
    private val vibrateChecked = MutableLiveData(UserStorage.vibrate)

    /**
     * 所有数据
     */
    val items = MutableLiveData<List<Any>>()

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
        map<ItemViewModelSettingSwitch>(BR.item, R.layout.nim_item_setting_switch)
    }

    /**
     * 初始化数据
     */
    override fun initData() {
        super.initData()
        val data = ArrayList<Any>()
        data.add(ItemViewModelLine(this))
        data.add(
            ItemViewModelSettingSwitch(this, R.string.ring, ringChecked) {
                val ring = ringChecked.value != true
                ringChecked.value = ring
                UserStorage.ring = ring
                NimConfigSDKOption.updateStatusBarNotificationConfig()
            }
        )
        data.add(
            ItemViewModelSettingSwitch(this, R.string.vibrate, vibrateChecked) {
                val vibrate = vibrateChecked.value != true
                vibrateChecked.value = vibrate
                UserStorage.vibrate = vibrate
                NimConfigSDKOption.updateStatusBarNotificationConfig()
            }
        )
        items.value = data
    }

}