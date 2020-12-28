package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.libs.tool.jumpEmail
import com.chuzi.android.libs.tool.jumpToPhonePanel
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.shared.ext.toString2
import com.chuzi.android.shared.global.AppGlobal.context
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 文本消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ItemViewModelMessageText(
    viewModel: ViewModelMessage,
    message: IMMessage
) : ItemViewModelMessageBase(viewModel, message) {

    val text = MutableLiveData<String>()

    val textSize = AppFactorySDK.getFontSizeLiveData(18f)

    init {
        text.value = message.content
    }

    val onClickLinkUrlCommand = createTypeCommand<String> {

    }

    val onClickLinkTelCommand = createTypeCommand<String> {
        jumpToPhonePanel(context, it)
    }

    val onClickLinkEmailConmmnd = createTypeCommand<String> {
        jumpEmail(context, "选择程序打开Email", it.toString2())
    }

}