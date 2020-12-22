package com.chuzi.android.nim.ui.session.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.libs.tool.replaceAt
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.domain.UseCaseGetSessionList
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolNimExtension
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSearch
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSession
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.map
import com.google.common.primitives.Longs
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.rxjava.rxlife.life
import me.tatarka.bindingcollectionadapter2.itembindings.OnItemBindClass

/**
 * desc 会话列表ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelSession : ViewModelBase<ArgDefault>() {

    private val useCaseGetSessionList by lazy { UseCaseGetSessionList() }

    private val itemViewModelNetwork = ItemViewModelNetwork(this)

    private val itemViewModelMultiport = ItemViewModelMultiport(this)

    val isLoading = MutableLiveData(true)

    /**
     * 会话数据
     */
    val sessionList: LiveData<List<ItemViewModelSession>> =
        Transformations.map(AppFactorySDK.sessionLiveData) {
            it.map { item ->
                ItemViewModelSession(this, item)
            }
        }

    /**
     * 所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map(sessionList) { input ->
            val list = ArrayList<Any>()
            list.add(itemViewModelNetwork)
            list.add(itemViewModelMultiport)
            list.add(ItemViewModelSearch(this))
            list.addAll(input)
            list
        }

    /**
     * 注册布局
     */
    val itemBinding = OnItemBindClass<Any>().apply {
        map<ItemViewModelSession>(BR.item, R.layout.nim_item_session)
        map<ItemViewModelSearch>(BR.item, R.layout.nim_item_search)
        map<ItemViewModelNetwork>(BR.item, R.layout.nim_item_network)
        map<ItemViewModelMultiport>(BR.item, R.layout.nim_item_multiport)
    }

    /**
     * Item长点击事件
     */
    val onItemLongClickEvent = SingleLiveEvent<ItemViewModelSession>()

    /**
     * Item点击事件
     */
    val onItemClickEvent = SingleLiveEvent<ItemViewModelSession>()

    /**
     * 第一次加载数据完成
     */
    val onLoadCompleteEvent = SingleLiveEvent<Unit>()

    /**
     * 加载会话数据
     */
    fun loadSessionList() {
        useCaseGetSessionList.execute(Unit)
            .map {
                handleRecentList(it.get())
            }
            .bindToSchedulers()
            .bindToException()
            .life(this)
            .subscribe(
                {
                    AppFactorySDK.sessionLiveData.value = it
                    onLoadCompleteEvent.call()
                },
                {
                    handleThrowable(it)
                }
            )
    }


    /**
     * 删除某个会话
     * @param session 会话
     */
    fun deleteSession(session: ItemViewModelSession) {
        val contract = session.contact
        msgService().deleteRecentContact(contract)
        msgService().clearChattingHistory(contract.contactId, contract.sessionType)
        AppFactorySDK.sessionLiveData.value?.let {
            AppFactorySDK.sessionLiveData.value = it - session.contact
        }
    }

    /**
     * 还原Item背景颜色
     */
    fun returnItemColor() {
        sessionList.value?.forEach {
            it.setItemNormalColor()
        }
    }

    /**
     * 重新刷新会话列表
     */
    fun refreshData(accounts: List<String>): List<RecentContact> {
        var data = AppFactorySDK.sessionLiveData.value ?: listOf()
        accounts.forEach { account ->
            val index = getIndexByList(data, account)
            data = if (index == -1) {
                data
            } else {
                data.replaceAt(index) { it }
            }
        }
        return data.sortedWith(comparator)
    }

    /**
     * 处理消息数据
     * @param list 最近会话数据
     */
    fun handleRecentList(list: List<RecentContact>): List<RecentContact> {
        var data = AppFactorySDK.sessionLiveData.value ?: listOf()
        list.forEach { item ->
            val index = getIndexByList(data, item.contactId)
            data = if (index == -1) {
                data + item
            } else {
                data.replaceAt(index) {
                    item
                }
            }
        }
        return data.sortedWith(comparator)
    }

    /**
     * 从集合中获取下标
     */
    private fun getIndexByList(data: List<RecentContact>, contactId: String): Int {
        data.forEachIndexed { index, recentContact ->
            if (recentContact.contactId == contactId) {
                return index
            }
        }
        return -1
    }

    /**
     * 修改网络状态栏文案
     */
    fun setNetWorkText(@StringRes textId: Int) {
        itemViewModelNetwork.title.value = textId
    }

    /**
     * 是否显示网络状态栏
     * @param isShown
     */
    fun showNetWorkBar(isShown: Boolean) {
        itemViewModelNetwork.isShown.value = isShown
    }

    /**
     * 修改多端登录文案
     */
    fun setMultiportText(text: Int) {
        itemViewModelMultiport.title.value = text
    }

    /**
     * 是否显示多端登录栏
     * @param isShown
     */
    fun showMultiportBar(isShown: Boolean) {
        itemViewModelMultiport.isShown.value = isShown
    }

    /**
     * 通过消息uuid获取该最近会话的下标位置
     */
    private fun getRecentContactByUuid(data: List<RecentContact>, uuid: String?): Int? {
        data.forEachIndexed { index, recentContact ->
            if (recentContact.recentMessageId == uuid) {
                return index
            }
        }
        return null
    }

    /**
     * 处理状态改变的消息
     */
    fun handleRecentMessage(message: IMMessage) {
        val data = AppFactorySDK.sessionLiveData.value ?: return
        val index = getRecentContactByUuid(data, message.uuid) ?: return
        AppFactorySDK.sessionLiveData.value = data.replaceAt(index) {
            it.msgStatus = message.status
            it
        }.sortedWith(comparator)
    }

    /**
     * 比较器
     */
    private val comparator: Comparator<RecentContact> = Comparator { left, right ->
        val longRight: Long = ToolNimExtension.getStickyLong(right)
        val longLeft: Long = ToolNimExtension.getStickyLong(left)
        //先比较置顶 后 比较时间
        if (longLeft != Long.MIN_VALUE && longRight == Long.MIN_VALUE) {
            -1
        } else if (longLeft == Long.MIN_VALUE && longRight != Long.MIN_VALUE) {
            1
        } else {
            Longs.compare(right.time.coerceAtLeast(longRight), left.time.coerceAtLeast(longLeft))
        }
    }


//    val diff = object : DiffUtil.ItemCallback<Any>() {
//
//        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
//            if (oldItem is ItemViewModelSearch && newItem is ItemViewModelSearch) {
//                return true
//            }
//            if (oldItem == newItem) {
//                return true
//            }
//            return false
//        }
//
//        override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
//            if (oldItem is ItemViewModelSession && newItem is ItemViewModelSession) {
//                return oldItem.content.value == newItem.content.value &&
//                        oldItem.isStick.value == newItem.isStick.value &&
//                        oldItem.name.value == newItem.name.value &&
//                        oldItem.number.value == newItem.number.value &&
//                        oldItem.avatar.value.diffEquals(newItem.avatar.value) &&
//                        oldItem.date.value == newItem.date.value &&
//                        oldItem.background.value == newItem.background.value
//            }
//            return true
//        }
//    }

}
