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
import com.github.promeg.pinyinhelper.Pinyin
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc
 * author: 朱子楚
 * time: 2020/6/2 5:40 PM
 * since: v 1.0.0
 */
class ItemViewModelFriend(
    viewModel: BaseViewModel<*>,
    nimUserInfo: NimUserInfo
) : ItemViewModelBase(viewModel) {

    val account: String = nimUserInfo.account

    val name = ToolUserInfo.getUserDisplayName(nimUserInfo.account).toString()

    val pinYin = Pinyin.toPinyin(name[0])[0].toUpperCase()

    val isGray: LiveData<Boolean> = AppFactorySDK.getGrayImageLiveData()

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>().apply {
        value = nimUserInfo.avatar
    }

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData(R.mipmap.nim_avatar_default)

    /**
     * 标题
     */
    val title = MutableLiveData<String>().apply {
        value = name
    }

    val onClickCommand = createCommand {
        navigate(
            RoutePath.Nim.ACTIVITY_NIM_MESSAGE,
            ArgMessage(nimUserInfo.account, SessionTypeEnum.P2P.value)
        )
    }

}