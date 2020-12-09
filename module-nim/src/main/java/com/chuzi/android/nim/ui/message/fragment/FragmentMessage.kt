package com.chuzi.android.nim.ui.message.fragment

import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentMessageBinding
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessage
import com.chuzi.android.nim.view.LayoutMessageBottom
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_DEFAULT
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_EMOJI
import com.chuzi.android.nim.view.LayoutMessageBottom.Companion.TYPE_MORE
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgMessage
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.widget.log.lumberjack.L
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum

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

    override fun initListener() {
        super.initListener()

        binding.messageBottom.onAudioRecordChangeFunc = {
            viewModel.recordTime.value = it.toString()
        }

        binding.messageBottom.onAudioUpdateCancelFunc = {
            updateAudioTip(it)
        }

        binding.messageBottom.onAudioCancelFunc = {
            viewModel.isShownRecord.value = false
            recordAnima.stop()
        }

        binding.messageBottom.onAudioSendFunc = {
            viewModel.isShownRecord.value = false
            recordAnima.stop()

        }

        binding.messageBottom.onStartRecordFunc = {
            viewModel.isShownRecord.value = true
            recordAnima.start()
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
     * 懒加载数据
     */
    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadData(
            MessageBuilder.createEmptyMessage(
                arg.contactId,
                SessionTypeEnum.typeOfValue(arg.sessionType),
                0
            ),
            isFirst = true
        )
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.keyCode == KeyEvent.KEYCODE_BACK) {
            if (TYPE_EMOJI == binding.messageBottom.getInputType() || TYPE_MORE == binding.messageBottom.getInputType()) {
                binding.messageBottom.setInputType(TYPE_DEFAULT)
                return true
            }
        }
        return false
    }


    override fun onStop() {
        super.onStop()
        binding.messageBottom.stopRecord(true)
    }
}