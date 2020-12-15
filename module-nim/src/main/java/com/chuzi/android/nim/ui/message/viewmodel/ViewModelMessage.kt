package com.chuzi.android.nim.ui.message.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.domain.UseCaseGetMessageList
import com.chuzi.android.nim.domain.UseCaseGetTeamInfo
import com.chuzi.android.nim.domain.UseCaseGetUserInfo
import com.chuzi.android.nim.domain.UseCaseSendMessage
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.databinding.qmui.QMUIAction
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.map
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.rxjava.rxlife.life
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */
class ViewModelMessage : ViewModelBase<ArgMessage>() {

    private val pageSize = 30

    private val useCaseGetMessageList by lazy {
        UseCaseGetMessageList()
    }

    private val useCaseGetUserInfo by lazy {
        UseCaseGetUserInfo()
    }

    private val useCaseGetTeamInfo by lazy {
        UseCaseGetTeamInfo()
    }

    private val useCaseSendMessage by lazy {
        UseCaseSendMessage()
    }

    val title = MutableLiveData("聊天")

    /**
     * 消息数据
     */
    val items = ObservableArrayList<Any>()

    /**
     * 多布局注册
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelMessageText>(BR.item, R.layout.nim_item_message_text)
        map<ItemViewModelMessageUnknown>(BR.item, R.layout.nim_item_message_unknown)
    }

    /**
     * 添加数据完成事件
     */
    val onAddMessageCompletedEvent = SingleLiveEvent<List<Any>>()

    /**
     * 是否显示录音布局
     */
    val isShownRecord = MutableLiveData(false)

    /**
     * 录音时间显示
     */
    val recordTime = MutableLiveData<String>()

    /**
     * 录音手指移动tip的背景色
     */
    val tipAudioBackground = MutableLiveData<Int>()

    /**
     * 录音tip文本
     */
    val tipAudioText = MutableLiveData<Int>()

    /**
     * 上拉刷新触发
     */
    val onRefreshCommand = createTypeCommand<QMUIAction> {
        loadMessageList((items[0] as ItemViewModelMessageBase).message, it)
    }

    /**
     * 加载消息数据
     *
     * @param anchor 查询开始消息
     * @param onCompleteFunc 加载完成
     */
    fun loadMessageList(
        anchor: IMMessage,
        action: QMUIAction? = null,
        onCompleteFunc: (() -> Unit)? = null
    ) {
        useCaseGetMessageList.execute(UseCaseGetMessageList.Parameters(anchor, pageSize))
            .map {
                handleMessageList(it.get())
            }
            .bindToSchedulers()
            .bindToException()
            .life(this)
            .subscribe(
                {
                    items.addAll(0, it)
                    action?.finish()
                    onCompleteFunc?.invoke()
                },
                {
                    it.printStackTrace()
                    handleThrowable(it)
                }
            )
    }

    /**
     * 加载用户详情信息
     */
    fun loadUserInfo() {
        useCaseGetUserInfo.execute(arg.contactId)
            .life(this)
            .subscribe(
                {
                    title.value = ToolUserInfo.getUserDisplayName(it.get().account)
                },
                {
                    handleThrowable(it)
                }
            )

    }

    /**
     * 加载群组详情信息
     */
    fun loadTeamInfo() {
        useCaseGetTeamInfo.execute(arg.contactId)
            .life(this)
            .subscribe(
                {
                    title.value = it.get().name
                },
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 发送消息
     */
    fun sendMessage(message: IMMessage, resend: Boolean = false) {
        addMessage(listOf(message), true)
        useCaseSendMessage.execute(UseCaseSendMessage.Parameters(message, resend))
            .life(this)
            .subscribe(
                {},
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 添加新消息
     *
     * @param list 消息集合
     * @param isEvent 是否开启添加完成事件
     */
    fun addMessage(list: List<IMMessage>, isEvent: Boolean) {
        val data = handleMessageList(list)
        addItemViewModel(data, isEvent)
    }

    /**
     * 添加新消息
     * @param data itemViewModel集合
     * @param isEvent 是否开启添加完成事件
     */
    private fun addItemViewModel(data: List<ItemViewModelMessageBase>, isEvent: Boolean) {
        data.forEach { item ->
            val index = items.indexOf(item)
            if (index != -1) {
                //已经存在
                items[index] = item
            } else {
                //不存在
                items.add(item)
            }
        }
        if (isEvent) {
            onAddMessageCompletedEvent.value = items
        }
    }

    /**
     * 处理消息集合返回数据模型集合
     * @param list 消息集合
     */
    private fun handleMessageList(list: List<IMMessage>): List<ItemViewModelMessageBase> {
        return list.map { item ->
            when (item.msgType) {
                MsgTypeEnum.text -> {
                    ItemViewModelMessageText(this, item)
                }
                else -> {
                    ItemViewModelMessageUnknown(this, item)
                }
            }
        }
    }

}