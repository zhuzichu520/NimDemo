package com.chuzi.android.nim.ui.message.viewmodel

import androidx.lifecycle.MutableLiveData
import com.netease.nimlib.sdk.msg.attachment.LocationAttachment
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc
 * author: 朱子楚
 * time: 2020/5/8 2:15 PM
 * since: v 1.0.0
 */
class ItemViewModelMessageLocation(
    viewModel: ViewModelMessage,
    message: IMMessage
) : ItemViewModelMessageBase(viewModel,message) {

    val location = MutableLiveData<String>().apply {
        value = (message.attachment as LocationAttachment).address
    }

}