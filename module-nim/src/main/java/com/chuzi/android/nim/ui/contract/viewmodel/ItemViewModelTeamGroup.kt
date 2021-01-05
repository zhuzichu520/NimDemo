package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.shared.base.ItemViewModelBase
import com.netease.nimlib.sdk.team.model.Team

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelTeamGroup(
    viewModel: BaseViewModel<*>,
    team: Team,
    onClickFunc: (() -> Unit)? = null
) : ItemViewModelBase(viewModel) {

    val isGray: LiveData<Boolean> = AppFactorySDK.getGrayImageLiveData()

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>().apply {
        value = R.mipmap.nim_avatar_group
    }

    val icon = MutableLiveData(team.icon)

    val name = MutableLiveData(team.name)

    val content = MutableLiveData<String>().apply {
        value = "${team.memberCount}人"
    }

    val onClickCommand = createCommand {
        onClickFunc?.invoke()
    }

}