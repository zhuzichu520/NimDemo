package com.chuzi.android.nim.ui.session.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.google.common.primitives.Longs
import com.chuzi.android.libs.tool.replaceAt
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.event.SingleLiveEvent
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.domain.UseCaseGetSessionList
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolSticky
import com.chuzi.android.nim.ui.NimShare
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSearch
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSession
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.ext.map
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

    /**
     * 会话数据
     */
    val sessionList = NimShare.sessionLiveData

    /**
     * 所有数据
     */
    val items: LiveData<List<Any>> =
        Transformations.map(sessionList) { input ->
            val list = ArrayList<Any>()
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
     * 加载会话数据
     */
    fun loadData() {
        useCaseGetSessionList.execute(Unit)
            .life(this)
            .subscribe(
                {
                    handleData(it.get())
                },
                {
                    handleThrowable(it)
                }
            )
    }

    private fun handleData(list: List<RecentContact>?) {
        var data = sessionList.value ?: listOf()
        list?.forEach { item ->
            val session = ItemViewModelSession(this, item)
            val position = data.indexOf(session)
            data = if (position == -1) {
                data + session
            } else {
                data.replaceAt(position) {
                    session
                }
            }
        }
        sessionList.value = data.sortedWith(comparator)
    }

    /**
     * 重新刷新会话列表
     */
    fun refreshData(accounts: List<String>) {
        accounts.forEach { account ->
            var position = -1
            val list = sessionList.value ?: listOf()
            list.forEachIndexed { index, item ->
                if (item.contactId == account) {
                    position = index
                    return@forEachIndexed
                }
            }
            if (position != -1) {
                sessionList.value = list.replaceAt(position) {
                    it.copy(viewModel = this)
                }
            }
        }
    }

    /**
     * 处理消息数据
     * @param list 最近会话数据
     */
    fun handleRecentList(list: List<RecentContact>) {
        var data = sessionList.value ?: listOf()
        list.forEach { item ->
            val session = ItemViewModelSession(this, item)
            val position = data.indexOf(session)
            data = if (position == -1) {
                data + session
            } else {
                data.replaceAt(position) {
                    session
                }
            }
        }
        sessionList.value = data.sortedWith(comparator)
    }

    /**
     * 删除某个会话
     * @param session 会话
     */
    fun deleteSession(session: ItemViewModelSession) {
        val contract = session.contact
        msgService().deleteRecentContact(contract)
        msgService().clearChattingHistory(contract.contactId, contract.sessionType)
        sessionList.value?.let {
            sessionList.value = it - session
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
     * 比较器
     */
    private val comparator: Comparator<ItemViewModelSession> = Comparator { left, right ->
        val longRight: Long = ToolSticky.getStickyLong(right.contact)
        val longLeft: Long = ToolSticky.getStickyLong(left.contact)
        //先比较置顶 后 比较时间
        if (longLeft != Long.MIN_VALUE && longRight == Long.MIN_VALUE) {
            -1
        } else if (longLeft == Long.MIN_VALUE && longRight != Long.MIN_VALUE) {
            1
        } else {
            Longs.compare(right.time.coerceAtLeast(longRight), left.time.coerceAtLeast(longLeft))
        }
    }

}
