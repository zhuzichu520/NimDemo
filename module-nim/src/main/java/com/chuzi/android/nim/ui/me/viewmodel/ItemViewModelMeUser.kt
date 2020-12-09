package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ItemViewModelBase
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:01 PM
 * since: v 1.0.0
 */
class ItemViewModelMeUser(
    viewModel: BaseViewModel<*>,
    userInfo: MutableLiveData<NimUserInfo>
) : ItemViewModelBase(viewModel) {

    /**
     * 头像
     */
    val avatar: LiveData<String> = Transformations.map(userInfo) {
        ToolUserInfo.getUserName(it.account)
    }

    /**
     * 姓名
     */
    val name: LiveData<String> = Transformations.map(userInfo) {
        ToolUserInfo.getUserDisplayName(it.account)
    }

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData(R.mipmap.nim_avatar_default)

    val onClickCommand = createCommand {

    }

}