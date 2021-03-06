package com.chuzi.android.nim.ui.session.fragment

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.tool.showView
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.core.event.LoginSyncDataStatusObserver
import com.chuzi.android.nim.core.event.NimEvent
import com.chuzi.android.nim.core.event.NimEventManager
import com.chuzi.android.nim.databinding.NimFragmentSessionBinding
import com.chuzi.android.nim.emoji.EmojiManager
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.nim.ui.main.viewmodel.ItemViewModelSession
import com.chuzi.android.nim.ui.session.viewmodel.ViewModelSession
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.databinding.view.setOnClickDoubleListener
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.entity.arg.ArgNim
import com.chuzi.android.shared.entity.enumeration.EnumNimType
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.chuzi.android.shared.ext.dp2px
import com.chuzi.android.shared.ext.onNextComplete
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.widget.log.lumberjack.L
import com.google.common.base.Optional
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.ClientType
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.rxjava.rxlife.life
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


/**
 * desc 会话列表页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_SESSION)
class FragmentSession : FragmentBase<NimFragmentSessionBinding, ViewModelSession, ArgDefault>(),
    View.OnClickListener {

    private var normalPopup: QMUIPopup? = null

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_session

    override fun initView() {
        super.initView()
        binding.recycler.itemAnimator = null
    }

    override fun initData() {
        super.initData()
        initNimSyncData()
        initGlobalSyncData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NimEventManager.registObserveOnlineStatus(true)
        NimEventManager.registObserveOtherClients(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        NimEventManager.registObserveOnlineStatus(false)
        NimEventManager.registObserveOtherClients(false)
    }

    override fun onResume() {
        super.onResume()
        /**
         * 页面关闭时打开推送
         */
        NIMClient.getService(MsgService::class.java).setChattingAccount(
            MsgService.MSG_CHATTING_ACCOUNT_ALL, SessionTypeEnum.None
        )

    }

    override fun onPause() {
        super.onPause()
        /**
         * 页面可见时关闭推送
         */
        NIMClient.getService(MsgService::class.java)
            .setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
    }

    override fun initListener() {
        super.initListener()
        setOnClickDoubleListener(this, binding.menu)
    }

    override fun initViewObservable() {
        super.initViewObservable()

        /**
         * 消息状态
         */
        RxBus.toObservable(NimEvent.OnMessageStatusEvent::class.java)
            .observeOn(Schedulers.io())
            .map {
                Optional.fromNullable(viewModel.handleRecentMessage(it.message))
            }
            .observeOn(AndroidSchedulers.mainThread())
            .life(viewLifecycleOwner)
            .subscribe {
                if (it.isPresent) {
                    AppFactorySDK.sessionLiveData.value = it.get()
                }
            }

        /**
         * 第一次加载数据完成
         */
        viewModel.onLoadCompleteEvent.observe(viewLifecycleOwner) {
            viewModel.isLoading.value = false
        }

        /**
         * 用户资料发生变化
         */
        RxBus.toObservable(NimEvent.OnUserInfoUpdateEvent::class.java)
            .observeOn(Schedulers.io())
            .map {
                viewModel.refreshData(it.list.map { userInfo ->
                    userInfo.account
                })
            }
            .observeOn(AndroidSchedulers.mainThread())
            .life(viewLifecycleOwner)
            .subscribe {
                AppFactorySDK.sessionLiveData.value = it
            }

        /**
         * 监听好友关系变化
         */
        RxBus.toObservable(NimEvent.OnAddedOrUpdatedFriendsEvent::class.java)
            .observeOn(Schedulers.io())
            .map {
                viewModel.refreshData(it.list.map { friend ->
                    friend.account
                })
            }
            .observeOn(AndroidSchedulers.mainThread())
            .life(viewLifecycleOwner)
            .subscribe {
                AppFactorySDK.sessionLiveData.value = it
            }

        /**
         * 监听最近会话变换
         */
        RxBus.toObservable(NimEvent.OnRecentContactEvent::class.java)
            .observeOn(Schedulers.io())
            .map {
                viewModel.handleRecentList(it.list)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .life(viewModel)
            .subscribe {
                AppFactorySDK.sessionLiveData.value = it
            }

        /**
         * 监听Message销毁事件
         */
        RxBus.toObservable(EventUI.OnMessageDestoryEvent::class.java)
            .life(viewModel)
            .subscribe {
                viewModel.returnItemColor()
            }

        /**
         * 会话列表变化，重新刷新未读书
         */
        viewModel.sessionList.observe(viewLifecycleOwner) {
            var number = 0
            it.forEach { item ->
                number += item.number.value ?: 0
            }
            AppFactorySDK.unReadNumber.value = number
        }

        /**
         * Item长点击事件
         */
        viewModel.onItemLongClickEvent.observe(viewLifecycleOwner) {
            showItemPopup(it)
        }

        /**
         * Item点击事件
         */
        viewModel.onItemClickEvent.observe(viewLifecycleOwner) {
            navigate(
                RoutePath.Nim.ACTIVITY_NIM_MESSAGE,
                arg = ArgMessage(it.contactId, it.contact.sessionType.value)
            )
        }

        /**
         * 皮肤样式切换事件
         */
        SkinManager.onSkinChangeListener.observe(viewLifecycleOwner) {
            viewModel.returnItemColor()
        }

        RxBus.toObservable(NimEvent.OnLineStatusEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                if (it.statusCode.wontAutoLogin()) {
                    AppFactorySDK.logout()
                    navigate(RoutePath.Nim.ACTIVITY_NIM_MAIN, ArgNim(EnumNimType.LOGOUT))
                } else {
                    when (it.statusCode) {
                        StatusCode.NET_BROKEN -> {
                            L.tag("StatusCode").i { "NET_BROKEN" }
                            viewModel.showNetWorkBar(true)
                            viewModel.setNetWorkText(R.string.net_broken)
                        }
                        StatusCode.UNLOGIN -> {
                            L.tag("StatusCode").i { "UNLOGIN" }
                            viewModel.showNetWorkBar(false)
                            viewModel.setNetWorkText(R.string.nim_status_unlogin)
                        }
                        StatusCode.CONNECTING -> {
                            L.tag("StatusCode").i { "CONNECTING" }
                            viewModel.showNetWorkBar(false)
                            viewModel.setNetWorkText(R.string.nim_status_connecting)
                        }
                        StatusCode.LOGINING -> {
                            L.tag("StatusCode").i { "LOGINING" }
                            viewModel.showNetWorkBar(false)
                            viewModel.setNetWorkText(R.string.nim_status_logining)
                        }
                        else -> {
                            L.tag("StatusCode").i { "ELSE" }
                            viewModel.showNetWorkBar(false)
                        }
                    }
                }
            }

        RxBus.toObservable(NimEvent.OnOtherClientsEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                val list = it.list
                if (list.isNullOrEmpty()) {
                    viewModel.showMultiportBar(false)
                } else {
                    val onlineClient = list[0]
                    when (onlineClient.clientType) {
                        ClientType.Windows, ClientType.MAC -> {
                            viewModel.setMultiportText(R.string.computer_version)
                            viewModel.showMultiportBar(true)
                        }
                        ClientType.Web -> {
                            viewModel.setMultiportText(R.string.web_version)
                            viewModel.showMultiportBar(true)
                        }
                        ClientType.iOS, ClientType.Android -> {
                            viewModel.setMultiportText(R.string.mobile_version)
                            viewModel.showMultiportBar(true)
                        }
                        else -> {
                            viewModel.showMultiportBar(false)
                        }
                    }
                }
            }
    }

    /**
     * 长点击显示操作弹窗
     */
    private fun showItemPopup(item: ItemViewModelSession) {
        val recycler = RecyclerView(requireContext())
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setup {
            withDataSource(
                dataSourceTypedOf(
                    SessionOptionModel(if (item.isStick.value == true) R.string.clear_sticky_top else R.string.on_sticky_top),
                    SessionOptionModel(if (item.isStick.value == true) R.string.on_disturb else R.string.clear_disturb),
                    SessionOptionModel(R.string.remove)
                )
            )
            withItem<SessionOptionModel, SessionOptionHolder>(R.layout.nim_layout_session_options) {
                onBind(::SessionOptionHolder) { _, item ->
                    name.setText(item.textId)
                }
                onClick {
                    when (this.item.textId) {
                        R.string.on_sticky_top -> {
                            item.addSticky()
                        }
                        R.string.clear_sticky_top -> {
                            item.removeStick()
                        }
                        R.string.on_disturb -> {

                        }
                        R.string.clear_disturb -> {

                        }
                        R.string.remove -> {
                            viewModel.deleteSession(item)
                        }
                    }
                    normalPopup?.dismiss()
                }
            }
        }
        item.viewRef.get()?.let {
            normalPopup = QMUIPopups.popup(context, QMUIDisplayHelper.dp2px(context, 180))
                .view(recycler)
                .onDismiss {
                    viewModel.returnItemColor()
                }
                .skinManager(QMUISkinManager.defaultInstance(context))
                .shadow(true)
                .arrow(true)
                .show(it)
        }
    }

    /**
     * 同步数据监听
     */
    private fun initNimSyncData() {
        val syncCompleted: Boolean = LoginSyncDataStatusObserver
            .observeSyncDataCompletedEvent {
                viewModel.loadSessionList()
            }
        if (!syncCompleted) {
            //如果数据没有同步完成，弹个进度Dialog
            showView(binding.loading)
        } else {
            //数据同步完成，直接加载数据
            viewModel.loadSessionList()
        }
    }

    /**
     * 异步初始化全局数据
     */
    private fun initGlobalSyncData() {
        createFlowable<Unit> {
            EmojiManager.initLoad(requireContext())
            onNextComplete(Unit)
        }.bindToSchedulers().subscribe {

        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.menu -> {
                showMenuPopup()
            }
        }
    }

    /**
     * 右上角+号点击弹窗
     */
    private fun showMenuPopup() {
        val recycler = RecyclerView(requireContext())
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setup {
            withDataSource(
                dataSourceTypedOf(
                    SessionMenuModel(R.string.rich_scan, R.mipmap.nim_ic_menu_rich_scan),
                    SessionMenuModel(R.string.call_normal_team, R.mipmap.nim_ic_menu_call_team),
                    SessionMenuModel(R.string.add_buddy, R.mipmap.nim_ic_menu_add_buddy)
                )
            )
            withItem<SessionMenuModel, SessionMenuHolder>(R.layout.nim_layout_session_menus) {
                onBind(::SessionMenuHolder) { _, item ->
                    name.setText(item.textId)
                    icon.setImageResource(item.iconId)
                }
                onClick {
                    when (this.item.textId) {
                        R.string.rich_scan -> {
                        }
                        R.string.call_normal_team -> {
                        }
                        R.string.add_buddy -> {

                        }
                    }
                    normalPopup?.dismiss()
                }
            }
        }

        normalPopup = QMUIPopups.popup(context, 150f.dp2px(context))
            .view(recycler)
            .skinManager(QMUISkinManager.defaultInstance(context))
            .offsetX(0 - 50f.dp2px(context))
            .shadow(true)
            .arrow(true)
            .show(binding.menu)
    }

    class SessionOptionHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text)
    }

    class SessionMenuHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    data class SessionOptionModel(
        @StringRes val textId: Int
    )

    data class SessionMenuModel(
        @StringRes val textId: Int,
        @DrawableRes val iconId: Int
    )

}
