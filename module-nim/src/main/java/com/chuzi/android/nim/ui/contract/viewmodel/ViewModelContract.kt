package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSearch
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.route.RoutePath
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc 通讯录ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelContract : ViewModelBase<ArgDefault>() {

    /**
     * 所有数据
     */
    val items: LiveData<List<Any>> = Transformations.map(AppFactorySDK.sessionLiveData) { list ->
        val data = ArrayList<Any>()
        data.add(ItemViewModelSearch(this))
        data.add(
            ItemViewModelContractGroup(
                this,
                R.string.new_frends,
                R.mipmap.nim_item_add_friends
            )
        )
        data.add(
            ItemViewModelContractGroup(
                this,
                R.string.my_friends,
                R.mipmap.nim_item_my_fridends
            ) {
                navigate(RoutePath.Nim.ACTIVITY_NIM_CONTRACT_FRIENDS)
            }
        )
        data.add(
            ItemViewModelContractGroup(
                this,
                R.string.team_group,
                R.mipmap.nim_item_team_group
            ) {
                navigate(RoutePath.Nim.ACTIVITY_NIM_CONTRACT_TEAMGROUPS)
            }
        )
        data.add(ItemViewModelLine(this))
        data.add(ItemViewModelContractHistoryTitle(this))
        val historyList = mutableListOf<ItemViewModelContractHistory>()
        list.forEach { item ->
            if (item.sessionType == SessionTypeEnum.P2P) {
                historyList.add(ItemViewModelContractHistory(this, item))
            }
        }
        data.addAll(historyList)
        data
    }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelContractGroup>(BR.item, R.layout.nim_item_contract_group)
        map<ItemViewModelContractHistory>(BR.item, R.layout.nim_item_contract_history)
        map<ItemViewModelContractHistoryTitle>(BR.item, R.layout.nim_item_contract_history_title)
        map<ItemViewModelSearch>(BR.item, R.layout.nim_item_search)
        map<ItemViewModelLine>(BR.item, R.layout.nim_item_line)
    }


}
