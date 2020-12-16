package com.chuzi.android.nim.ui.message.viewmodel

import android.util.LayoutDirection
import android.view.View
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.storage.AppStorage
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import java.lang.ref.WeakReference

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/14 8:47 PM
 * since: v 1.0.0
 */
open class ItemViewModelMessageBase(
    viewModel: ViewModelMessage,
    val message: IMMessage
) : ItemViewModelBase(viewModel) {

    companion object {
        //发送中
        private const val STATE_SEND_LOADING = 0

        //发送失败
        private const val STATE_SEND_FAILED = 1

        //发送完成
        private const val STATE_SEND_NORMAL = 2

        //发送中
        const val STATE_ATTACH_LOADING = 0

        //发送失败
        private const val STATE_ATTACH_FAILED = 1

        //发送完成
        private const val STATE_ATTACH_NORMAL = 2
    }

    private val userInfo = ToolUserInfo.getUserInfo(message.fromAccount)

    /**
     * 会话id
     */
    val uuid: String = message.uuid

    /**
     * 姓名
     */
    val name = MutableLiveData<String>().apply {
        value = userInfo?.name
    }

    /**
     * 头像
     */
    val avatar = MutableLiveData<String>().apply {
        value = userInfo?.avatar
    }


    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>().apply {
        value = R.mipmap.nim_avatar_default
    }

    /**
     * 是否显示姓名
     */
    val displayName = MutableLiveData<Int>().apply {
        value = if (message.sessionType == SessionTypeEnum.Team && !isMine()) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    /**
     * 类容背景
     */
    val background = MutableLiveData<Int>().apply {
        value = if (isMine())
            R.drawable.nim_chat_to_bg_selector
        else
            R.drawable.nim_chat_from_bg_selector
    }

    /**
     * 布局方向
     */
    val layoutDirection = MutableLiveData<Int>().apply {
        value = if (isMine()) LayoutDirection.RTL else LayoutDirection.LTR
    }

    /**
     * 发送状态
     */
    val messageStatus = MutableLiveData(STATE_SEND_LOADING).apply {
        value = when (message.status) {
            MsgStatusEnum.fail -> {
                STATE_SEND_FAILED
            }
            MsgStatusEnum.sending -> {
                STATE_SEND_LOADING
            }
            else -> {
                STATE_SEND_NORMAL
            }
        }
    }

    /**
     * 附件下载状态
     */
    val attachStatus = MutableLiveData<Int>().apply {
        value = when (message.attachStatus) {
            AttachStatusEnum.transferring -> {
                STATE_ATTACH_LOADING
            }
            AttachStatusEnum.fail -> {
                if (message.status != MsgStatusEnum.fail) {
                    STATE_ATTACH_FAILED
                } else {
                    STATE_ATTACH_NORMAL
                }
            }
            else -> {
                STATE_ATTACH_NORMAL
            }
        }
    }

    /**
     * 当前View，用来记录长按点击弹出位置
     */
    var viewRef = WeakReference<View>(null)


    val progress = MutableLiveData<String>()

    /**
     * 长点击
     */
    val onLongClickCommand = createTypeCommand<View> {
        viewRef = WeakReference(it)
        viewModel.onItemLongClickEvent.value = this
    }

    /**
     * 重新发送消息
     */
    val onClickResendCommand = createCommand {
        message.status = MsgStatusEnum.sending
        message.attachStatus = AttachStatusEnum.def
        viewModel.sendMessage(message, true)
    }

    fun isMine(): Boolean {
        return message.fromAccount == AppStorage.account
    }

}