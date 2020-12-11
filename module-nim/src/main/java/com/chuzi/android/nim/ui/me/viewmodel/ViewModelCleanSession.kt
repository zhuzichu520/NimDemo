package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelLine
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
class ViewModelCleanSession : ViewModelBase<ArgDefault>() {

    val all = MutableLiveData(false)

    /**
     * 会话数据
     */
    private val sessionList = MutableLiveData<List<ItemViewModelCacheSession>>()

    /**
     * 所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map(sessionList) { input ->
            val list = ArrayList<Any>()
            list.add(ItemViewModelLine(this))
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelCacheSession>(BR.item, R.layout.nim_item_cache_session)
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
    }

    /**
     * 点击全选事件
     */
    val onClickCommand = createCommand {
        val isAll = all.value != true
        updateAllSeleted(isAll)
        all.value = isAll
    }

    /**
     * 更新所有选择项
     */
    private fun updateAllSeleted(isSeleted: Boolean) {
        sessionList.value?.forEach {
            it.isSelected.value = isSeleted
        }
    }

    /**
     * 判断是否全选
     */
    fun checkAll() {
        val count = sessionList.value?.size
        var selectedSize = 0
        sessionList.value?.forEach { item ->
            if (item.isSelected.value == true) {
                selectedSize += 1
            }
        }
        all.value = count == selectedSize
    }

    /**
     * 清除选择会话
     */
    fun cleanSession() {
        sessionList.value?.forEach { item ->
            val contract = item.contact
            if (item.isSelected.value == true) {
                msgService().deleteRecentContact(contract)
                msgService().clearChattingHistory(contract.contactId, contract.sessionType)
                AppFactorySDK.sessionLiveData.value?.let {
                    AppFactorySDK.sessionLiveData.value = it - contract
                }
                sessionList.value?.let {
                    sessionList.value = it - item
                }
            }
        }
    }

    /**
     * 加载会话数据
     */
    fun loadData() {
        AppFactorySDK.sessionLiveData.value?.let {
            sessionList.value = it.map { item ->
                ItemViewModelCacheSession(this, item)
            }
        }
    }
}