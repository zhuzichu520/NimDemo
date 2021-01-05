package com.chuzi.android.nim.ui.contract.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.route.RoutePath
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

    val isGray: LiveData<Boolean> = AppFactorySDK.getGrayImageLiveData()

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


    val onClickCommand = createCommand {
        navigate(
            RoutePath.Nim.ACTIVITY_NIM_MESSAGE,
            ArgMessage(contactId = contact.contactId, contact.sessionType.value)
        )
    }

}