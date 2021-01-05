package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.domain.UseCaseGetTeams
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.entity.arg.ArgGroupList
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.storage.AppStorage
import com.rxjava.rxlife.life
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 5:40 PM
 * since: v 1.0.0
 */
class ViewModelGroupList : ViewModelBase<ArgGroupList>() {

    private val useCaseGetTeams by lazy { UseCaseGetTeams() }

    /**
     * 所有数据
     */
    val items = MutableLiveData<List<Any>>()

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelTeamGroup>(BR.item, R.layout.nim_item_team_group)
    }

    /**
     * 获取群组列表
     */
    fun loadTeamList() {
        useCaseGetTeams.execute(Unit).map {
            if (arg.isOwner) {
                it.filter { team ->
                    team.creator == AppStorage.account
                }
            } else {
                it.filter { team ->
                    team.creator != AppStorage.account
                }
            }
        }.life(this).subscribe {
            items.value = it.map { team ->
                ItemViewModelTeamGroup(this, team)
            }
        }
    }

}