package com.chuzi.android.nim.ui.message.fragment

import android.Manifest
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.drawable.AnimationDrawable
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afollestad.recyclical.ViewHolder
import com.afollestad.recyclical.datasource.dataSourceTypedOf
import com.afollestad.recyclical.setup
import com.afollestad.recyclical.withItem
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.alpha
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.core.attachment.AttachmentSticker
import com.chuzi.android.nim.core.event.NimEvent
import com.chuzi.android.nim.databinding.NimFragmentMessageBinding
import com.chuzi.android.nim.emoji.StickerItem
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolNimExtension
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.nim.ui.message.viewmodel.ItemViewModelMessageBase
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessage
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_DEFAULT
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_EMOJI
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_MORE
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.entity.arg.ArgPermissions
import com.chuzi.android.shared.global.CacheGlobal
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.skin.SkinManager
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.MsgService
import com.netease.nimlib.sdk.msg.constant.AttachStatusEnum
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.qmuiteam.qmui.skin.QMUISkinManager
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.popup.QMUIPopup
import com.qmuiteam.qmui.widget.popup.QMUIPopups
import com.rxjava.rxlife.life
import com.tbruyelle.rxpermissions3.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_P2P)
class FragmentMessage : FragmentBase<NimFragmentMessageBinding, ViewModelMessage, ArgMessage>() {

    companion object {
        private const val REQUEST_CODE_CHOOSE = 0x11
    }

    private val fragmentOperation: Fragment by lazy {
        FragmentMessageOperation()
    }

    private val fragmentEmoticon: Fragment by lazy {
        FragmentMessageEmoticon()
    }

    private var currentFragment: Fragment? = null

    private var normalPopup: QMUIPopup? = null

    private lateinit var recordAnima: AnimationDrawable

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_message

    override fun initView() {
        super.initView()
        binding.recycler.itemAnimator = null
        binding.messageBottom.attachContentView(binding.layoutContent, binding.recycler)
        binding.messageBottom.setInputType(TYPE_DEFAULT)
        recordAnima = binding.viewRecord.background as AnimationDrawable
        getRecentContract()?.let {
            binding.messageBottom.setText(ToolNimExtension.getDraft(it))
        }

    }

    override fun initLazyData() {
        super.initLazyData()
        loadData(arg.sessionTypeEnum())
        viewModel.loadMessageList(
            MessageBuilder.createEmptyMessage(
                arg.contactId,
                arg.sessionTypeEnum(),
                0
            )
        ) {
            scrollToBottom()
            MainHandler.postDelayed {
                alpha(binding.layoutContent, 300, f = floatArrayOf(0f, 1f))
            }
        }
    }

    /**
     * 加载相应的会话数据
     * @param sessionType 会话类型
     */
    private fun loadData(sessionType: SessionTypeEnum) {
        when (sessionType) {
            SessionTypeEnum.P2P -> {
                viewModel.loadUserInfo()
            }
            SessionTypeEnum.Team -> {
                viewModel.loadTeamInfo()
            }
            else -> {

            }
        }
    }

    override fun onResume() {
        super.onResume()
        msgService().setChattingAccount(arg.contactId, arg.sessionTypeEnum())
    }

    override fun onPause() {
        super.onPause()
        msgService().setChattingAccount(MsgService.MSG_CHATTING_ACCOUNT_NONE, SessionTypeEnum.None)
    }

    override fun initListener() {
        super.initListener()

        /**
         * 录音时间监听
         */
        binding.messageBottom.onAudioRecordChangeFunc = {
            viewModel.recordTime.value = it.toString()
        }

        /**
         * 录音手指移动是否取消监听
         */
        binding.messageBottom.onAudioUpdateCancelFunc = {
            updateAudioTip(it)
        }

        /**
         * 录音取消
         */
        binding.messageBottom.onAudioCancelFunc = {
            viewModel.isShownRecord.value = false
            recordAnima.stop()
        }

        /**
         * 录音发送
         */
        binding.messageBottom.onAudioSendFunc = {
            viewModel.isShownRecord.value = false
            recordAnima.stop()

        }

        /**
         * 开始录音
         */
        binding.messageBottom.onStartRecordFunc = {
            viewModel.isShownRecord.value = true
            recordAnima.start()
        }

        /**
         * 点击发送按钮
         */
        binding.messageBottom.onTextSendFunc = {
            sendTextMessage(it)
        }

    }

    /**
     * 更新录音tip
     */
    private fun updateAudioTip(cancelled: Boolean) {
        if (cancelled) {
            viewModel.tipAudioText.value = R.string.recording_cancel_tip
            viewModel.tipAudioBackground.value = R.drawable.nim_bg_cancel_record
        } else {
            viewModel.tipAudioText.value = R.string.recording_cancel
            viewModel.tipAudioBackground.value = 0
        }
    }


    override fun initViewObservable() {
        super.initViewObservable()

        binding.messageBottom.onShowOperationFunc = {
            switchFragment(it, fragmentOperation)
        }

        binding.messageBottom.onShowEmoticonFunc = {
            switchFragment(it, fragmentEmoticon)
        }

        /**
         * 点击贴图
         */
        RxBus.toObservable(EventUI.OnClickStickerEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                sendStickerMessage(it.sticker)
            }

        /**
         * 点击删除Emoji
         */
        RxBus.toObservable(EventUI.OnClickEmojiDeleteEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                binding.messageBottom.dropText()
            }

        /**
         * 点击Emoji
         *
         */
        RxBus.toObservable(EventUI.OnClickEmojiEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                binding.messageBottom.appendText(it.text)
            }

        /**
         * 消息接收监听
         */
        RxBus.toObservable(NimEvent.OnReceiveMessageEvent::class.java)
            .map {
                it.list.filter { item ->
                    item.sessionId == arg.contactId
                }
            }
            .map {
                viewModel.handleMessageList(it)
            }
            .life(viewLifecycleOwner)
            .subscribe {
                viewModel.addItemViewModel(it, true)
            }

        /**
         * 消息状态监听
         */
        RxBus.toObservable(NimEvent.OnMessageStatusEvent::class.java)
            .map {
                it.message
            }
            .filter {
                it.sessionId == arg.contactId
            }
            .map {
                viewModel.handleMessageList(listOf(it))
            }
            .life(viewLifecycleOwner)
            .subscribe {
                viewModel.addItemViewModel(it, false)
            }

        /**
         * 发送地图消息
         */
        RxBus.toObservable(EventUI.OnSendLocationEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                val latLon = it.item.latLonPoint
                val message = MessageBuilder.createLocationMessage(
                    arg.contactId,
                    arg.sessionTypeEnum(),
                    latLon.latitude,
                    latLon.longitude,
                    it.item.poiItem.title
                )
                viewModel.sendMessage(message)
            }

        RxBus.toObservable(EventUI.OnClickOperationEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                when (it.titleId) {
                    R.string.message_operation_album -> {
                        //相册
                        startAlbum()
                    }
                    R.string.message_operation_video -> {
                    }
                    R.string.message_operation_local -> {
                        startLocation()
                    }
                    R.string.message_operation_file -> {
                    }
                    R.string.message_operation_call -> {
                    }
                }
            }

        RxBus.toObservable(NimEvent.OnAttachmentProgressEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                val attachment = it.attachment
                val index = viewModel.getIndexByUuid(attachment.uuid) ?: return@subscribe
                val itemViewModel = viewModel.getItemViewModelByIndex(index) ?: return@subscribe
                val percent = attachment.transferred.toFloat() / attachment.total.toFloat()
                itemViewModel.progress.value = String.format("%d%%", (percent * 100).toInt())
            }

        /**
         * 添加数据完成
         */
        viewModel.onAddMessageCompletedEvent.observe(viewLifecycleOwner, {
            if (isLastVisible()) {
                //最后一条可见 滑动到底部
                scrollToBottom()
            } else {
                //todo zhuzichu 最后一条不可见 显示提醒有新消息
            }
        })

        viewModel.onItemLongClickEvent.observe(viewLifecycleOwner) {
            showItemPopup(it)
        }

    }

    /**
     * 发送贴图消息
     */
    private fun sendStickerMessage(sticker: StickerItem) {
        val attachment = AttachmentSticker(sticker.category, sticker.name)
        val stickerMessage = MessageBuilder.createCustomMessage(
            arg.contactId, arg.sessionTypeEnum(), "贴图消息", attachment
        )
        viewModel.sendMessage(stickerMessage)
    }

    /**
     * 消息长按弹窗
     */
    private fun showItemPopup(item: ItemViewModelMessageBase) {
        val recycler = RecyclerView(requireContext())
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setup {
            withDataSource(
                dataSourceTypedOf(
                    MessageOptionModel(R.string.copy),
                    MessageOptionModel(R.string.delete),
                )
            )
            withItem<MessageOptionModel, MessageOptionHolder>(R.layout.nim_layout_message_options) {
                onBind(::MessageOptionHolder) { _, item ->
                    name.setText(item.textId)
                }
                onClick {
                    when (this.item.textId) {
                        R.string.copy -> {
                        }
                    }
                    normalPopup?.dismiss()
                }
            }
        }
        item.viewRef.get()?.let {
            normalPopup = QMUIPopups.popup(context, QMUIDisplayHelper.dp2px(context, 140))
                .view(recycler)
                .skinManager(QMUISkinManager.defaultInstance(context))
                .shadow(true)
                .arrow(true)
                .show(it)
        }
    }

    /**
     * 更换底部Fragment
     */
    private fun switchFragment(viewId: Int, fragment: Fragment) {
        if (fragment == currentFragment)
            return
        val transaction = parentFragmentManager.beginTransaction()
        if (fragment.isAdded) {
            transaction.show(fragment)
        } else {
            transaction.add(viewId, fragment)
        }
        currentFragment?.let {
            transaction.hide(it)
        }
        transaction.commit()
        this.currentFragment = fragment
    }

    /**
     * 重写返回键
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            if (TYPE_EMOJI == binding.messageBottom.getInputType() || TYPE_MORE == binding.messageBottom.getInputType()) {
                binding.messageBottom.setInputType(TYPE_DEFAULT)
                return true
            }
        }
        return false
    }

    /**
     * 发送文本消息
     */
    private fun sendTextMessage(text: String) {
        val message = MessageBuilder.createTextMessage(arg.contactId, arg.sessionTypeEnum(), text)
        viewModel.sendMessage(message)
    }

    /**
     * 停止录音
     */
    override fun onStop() {
        super.onStop()
        binding.messageBottom.stopRecord(true)
    }

    /**
     * 滑动到置顶位置
     */
    private fun scrollToBottom() {
        MainHandler.postDelayed(50) {
            binding.recycler.scrollBy(0, Int.MAX_VALUE)
        }
    }

    /**
     * 判断最后一条是否可见
     */
    private fun isLastVisible(): Boolean {
        val position = viewModel.items.size - 1
        val findLastPosition =
            (binding.recycler.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() + 1
        return findLastPosition == position
    }

    /**
     * 跳转到相册
     */
    private fun startAlbum() {
        RxPermissions(requireActivity()).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).life(viewLifecycleOwner).subscribe {
            if (it) {
                Matisse.from(this)
                    .choose(MimeType.of(MimeType.JPEG, MimeType.PNG, MimeType.GIF))
                    .countable(true)
                    .maxSelectable(9)
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.85f)
                    .imageEngine(GlideEngine())
                    .theme(if (SkinManager.isDark()) R.style.Matisse_Dracula else R.style.Matisse_Zhihu)
                    .showPreview(false) // Default is `true`
                    .forResult(REQUEST_CODE_CHOOSE)
            } else {
                navigate(RoutePath.DIALOG_PERMISSIONS_MAIN, arg = ArgPermissions("文件读写"))
            }
        }
    }

    /**
     * 跳转到地图界面
     */
    private fun startLocation() {
        RxPermissions(requireActivity()).request(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).life(viewLifecycleOwner).subscribe {
            if (it) {
                navigate(RoutePath.Map.ACTIVITY_MAP_LOCATION)
            } else {
                navigate(RoutePath.DIALOG_PERMISSIONS_MAIN, arg = ArgPermissions("定位"))
            }
        }
    }


    /**
     * 发送图片消息
     */
    private fun sendImageMessage(paths: List<String>) {
        Luban.with(requireContext())
            .load(paths)
            .ignoreBy(100)
            .setTargetDir(CacheGlobal.getLubanCacheDir())
            .filter {
                !(TextUtils.isEmpty(it) || it.toLowerCase(Locale.getDefault()).endsWith(".gif"))
            }.setCompressListener(object : OnCompressListener {
                override fun onSuccess(file: File) {
                    val message =
                        MessageBuilder.createImageMessage(
                            arg.contactId,
                            arg.sessionTypeEnum(),
                            file
                        )
                    message.attachStatus = AttachStatusEnum.transferring
                    viewModel.sendMessage(message, false)
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

                override fun onStart() {

                }
            }).launch()
    }

    override fun onDestroyView() {
        handleDraft()
        super.onDestroyView()
    }

    /**
     * 获取当前会话
     */
    private fun getRecentContract(): RecentContact? {
        val data = AppFactorySDK.sessionLiveData.value ?: return null
        data.forEach {
            if (it.contactId == arg.contactId) {
                return it
            }
        }
        return null
    }

    /**
     * 处理会话草稿
     */
    private fun handleDraft() {
        getRecentContract()?.let {
            val text = binding.messageBottom.getText()
            if (text.isNullOrBlank()) {
                ToolNimExtension.removeDraft(it)
                return
            }
            ToolNimExtension.addDraft(it, text)
        }
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                sendImageMessage(Matisse.obtainPathResult(it))
            }
        }
    }

    data class MessageOptionModel(
        @StringRes val textId: Int
    )

    class MessageOptionHolder(itemView: View) : ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.text)
    }

}