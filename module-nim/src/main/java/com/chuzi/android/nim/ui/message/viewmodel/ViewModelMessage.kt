package com.chuzi.android.nim.ui.message.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.libs.tool.toCast
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.core.attachment.AttachmentSticker
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.domain.UseCaseGetMessageList
import com.chuzi.android.nim.domain.UseCaseGetTeamInfo
import com.chuzi.android.nim.domain.UseCaseGetUserInfo
import com.chuzi.android.nim.domain.UseCaseSendMessage
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.databinding.qmui.QMUIAction
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.ext.map
import com.chuzi.android.shared.storage.AppStorage
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.team.model.Team
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
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
     * Item长点击事件
     */
    val onItemLongClickEvent = SingleLiveEvent<ItemViewModelMessageBase>()

    /**
     * 消息数据
     */
    val items = ObservableArrayList<Any>()

    /**
     * 多布局注册
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelMessageText>(BR.item, R.layout.nim_item_message_text)
        map<ItemViewModelMessageImage>(BR.item, R.layout.nim_item_message_image)
        map<ItemViewModelMessageLocation>(BR.item, R.layout.nim_item_message_location)
        map<ItemViewModelMessageSticker>(BR.item, R.layout.nim_item_message_sticker)
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
        useCaseGetMessageList.execute(UseCaseGetMessageList.Parameters(anchor, pageSize,
            NimRequestCallback(
                {
                    val messages = it ?: return@NimRequestCallback
                    val data = handleMessageList(messages)
                    addItemViewModel(data, false, true)
                    action?.finish()
                    onCompleteFunc?.invoke()
                }
            )))
    }

    /**
     * 加载用户详情信息
     */
    fun loadUserInfo() {
        ToolUserInfo.getUserInfo(AppStorage.account)?.let {
            updateInfoData(it, null)
        } ?: run {
            useCaseGetUserInfo.execute(
                UseCaseGetUserInfo.Parameters(
                    listOf(AppStorage.account),
                    NimRequestCallback({
                        updateInfoData(it?.get(0), null)
                    })
                )
            )
        }
    }


    /**
     * 加载群组详情信息
     */
    fun loadTeamInfo() {
        useCaseGetTeamInfo.execute(arg.contactId)
            .life(this)
            .subscribe(
                {
                    updateInfoData(null, it.get())
                },
                {
                    handleThrowable(it)
                }
            )
    }

    /**
     * 更新用户或者群组数据UI
     */
    private fun updateInfoData(userInfo: NimUserInfo?, team: Team?) {
        userInfo?.let {
            title.value = ToolUserInfo.getUserDisplayName(userInfo.account)
        }
        team?.let {
            title.value = it.name
        }
    }

    /**
     * 发送消息
     */
    fun sendMessage(message: IMMessage, resend: Boolean = false) {
        //如果是重发，则删除当前Message然后重新添加
        if (resend) {
            getIndexByUuid(message.uuid)?.let {
                items.removeAt(it)
            }
        }
        addItemViewModel(handleMessageList(listOf(message)), true)
        useCaseSendMessage.execute(UseCaseSendMessage.Parameters(message, resend,
            NimRequestCallback(
                {

                }
            )
        ))
    }

    /**
     * 添加新消息
     * @param data itemViewModel集合
     * @param isEvent 是否开启添加完成事件
     */
    fun addItemViewModel(
        data: List<ItemViewModelMessageBase>,
        isEvent: Boolean,
        isLoad: Boolean? = null
    ) {
        (if (isLoad == true) data.reversed() else data).forEach { item ->
            val index = getIndexByUuid(item.uuid)
            if (index != null) {
                //已经存在
                items[index] = handleMessageItem(item, index - 1)
            } else {
                //不存在
                if (isLoad == true) {
                    items.add(0, handleMessageItem(item, 0))
                } else {
                    items.add(handleMessageItem(item, items.size - 1))
                }
            }
        }
        checkLoadAttachment(data)
        if (isEvent) {
            onAddMessageCompletedEvent.value = items
        }
    }

    /**
     * 处理Item的anchorMessage一些操作
     */
    private fun handleMessageItem(
        item: ItemViewModelMessageBase,
        previousIndex: Int
    ): ItemViewModelMessageBase {
        (items.getOrNull(previousIndex) as? ItemViewModelMessageBase)?.let {
            item.anchorMessage = it.message
        }
        return item
    }

    /**
     * 移除某一条消息
     */
    fun removeMessageItem(item: ItemViewModelMessageBase) {
        items.remove(item)
        msgService().deleteChattingHistory(item.message)
    }


    /**
     * 消息下载附件处理
     */
    private fun checkLoadAttachment(list: List<ItemViewModelMessageBase>) {
        list.forEach { item ->
            when (item) {
                is ItemViewModelMessageImage -> {
                    item.checkDownLoadAttachment()
                }
            }
        }
    }

    /**
     * todo 用工厂模式替代when
     * 处理消息集合返回数据模型集合
     * @param list 消息集合
     */
    fun handleMessageList(list: List<IMMessage>): List<ItemViewModelMessageBase> {
        return list.map { item ->
            when (item.msgType) {
                MsgTypeEnum.text -> {
                    ItemViewModelMessageText(this, item)
                }
                MsgTypeEnum.location -> {
                    ItemViewModelMessageLocation(this, item)
                }
                MsgTypeEnum.image -> {
                    ItemViewModelMessageImage(this, item)
                }
                MsgTypeEnum.custom -> {
                    val attachment = item.attachment
                    if (attachment is AttachmentSticker) {
                        ItemViewModelMessageSticker(this, item)
                    } else {
                        ItemViewModelMessageUnknown(this, item)
                    }
                }
                else -> {
                    ItemViewModelMessageUnknown(this, item)
                }
            }
        }
    }

    /**
     * 通过Message获取下标位置
     */
    fun getIndexByUuid(uuid: String): Int? {
        items.forEachIndexed { index, item ->
            if ((item as ItemViewModelMessageBase).uuid == uuid) {
                return index
            }
        }
        return null
    }

    /**
     * 通过下标获取ItemViewModel
     */
    fun getItemViewModelByIndex(index: Int): ItemViewModelMessageBase? {
        if (index < 0) {
            return null
        }
        if (index >= items.size) {
            return null
        }
        return items[index].toCast()
    }

}