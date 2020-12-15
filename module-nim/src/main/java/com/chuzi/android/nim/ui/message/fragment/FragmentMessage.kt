package com.chuzi.android.nim.ui.message.fragment

import android.graphics.drawable.AnimationDrawable
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.alpha
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.core.event.NimEvent
import com.chuzi.android.nim.databinding.NimFragmentMessageBinding
import com.chuzi.android.nim.ui.event.EventUI
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessage
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_DEFAULT
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_EMOJI
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_MORE
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.bus.RxBus
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.route.RoutePath
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.rxjava.rxlife.life

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_P2P)
class FragmentMessage : FragmentBase<NimFragmentMessageBinding, ViewModelMessage, ArgMessage>() {

    private val fragmentOperation: Fragment by lazy {
        FragmentMessageOperation()
    }

    private val fragmentEmoticon: Fragment by lazy {
        FragmentMessageEmoticon()
    }

    private var currentFragment: Fragment? = null

    private lateinit var recordAnima: AnimationDrawable

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_message

    override fun initView() {
        super.initView()
        binding.messageBottom.attachContentView(binding.layoutContent, binding.recycler)
        binding.messageBottom.setInputType(TYPE_DEFAULT)
        recordAnima = binding.viewRecord.background as AnimationDrawable
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

        RxBus.toObservable(EventUI.OnClickStickerEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {

            }

        RxBus.toObservable(EventUI.OnClickEmojiDeleteEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                binding.messageBottom.dropText()
            }

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
            .life(viewLifecycleOwner)
            .subscribe {
                viewModel.addMessage(it, true)
            }

        /**
         * 消息状态监听
         */
        RxBus.toObservable(NimEvent.OnMessageStatusEvent::class.java)
            .life(viewLifecycleOwner)
            .subscribe {
                val message = it.message
                if (message.sessionId == arg.contactId) {
                    viewModel.addMessage(listOf(it.message), false)
                }
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
}