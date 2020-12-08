package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.nim.R
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ItemViewModelBase
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 8:54 PM
 * since: v 1.0.0
 */
class ItemViewModelContractHistory(
    viewModel: BaseViewModel<*>,
    contact: RecentContact
) : ItemViewModelBase(viewModel) {

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>().apply {
        value = ToolUserInfo.getUserInfo(contact.contactId)?.avatar
    }

    /**
     * 会话标题
     */
    val name = MutableLiveData<String>().apply {
        value = ToolUserInfo.getUserDisplayName(contact.contactId)
    }

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData(R.mipmap.nim_avatar_default)

}