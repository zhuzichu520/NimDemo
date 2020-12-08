package com.chuzi.android.nim.view

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.*
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimLayoutMessageBottomBinding
import com.chuzi.android.nim.emoji.ToolMoon
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.changeWidth
import com.jakewharton.rxbinding4.view.touches
import com.rxjava.rxlife.life
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/13 9:38 AM
 * since: v 1.0.0
 * todo 待优化，还是不太流畅
 */
class LayoutMessageBottom @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 根布局
     */
    private lateinit var contentView: View

    /**
     * 动画时间
     */
    private val duration = 50L

    /**
     * 类型
     */
    private var inputType: Int = TYPE_DEFAULT

    /**
     * 消息列表的RecyclerView
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * 显示底部Emoji界面事件
     */
    var onShowEmoticonFunc: ((Int) -> Unit)? = null

    /**
     * 显示底部更多界面事件
     */
    var onShowOperationFunc: ((Int) -> Unit)? = null

    /**
     * 手指移动取消改变事件
     */
    var onAudioUpdateCancelFunc: ((Boolean) -> Unit)? = null

    /**
     * 手指移动取消录音事件
     */
    var onAudioCancelFunc: (() -> Unit)? = null

    /**
     * 手指移动确认发送录音事件
     */
    var onAudioSendFunc: (() -> Unit)? = null

    /**
     * 录音读数改变事件
     */
    var onAudioRecordChangeFunc: ((Int) -> Unit)? = null

    /**
     * 开启录音事件
     */
    var onStartRecordFunc: (() -> Unit)? = null

    /**
     * 录音秒数
     */
    private var recordSecond = 0

    /**
     * 是否打开录音
     */
    private var isOpenRecord = false

    /**
     * 是否能取消录音
     */
    private var cancelled: Boolean = false

    /**
     * 计时器的Disposable对象
     */
    private var disposable: Disposable? = null

    private val binding by lazy {
        DataBindingUtil.inflate<NimLayoutMessageBottomBinding>(
            LayoutInflater.from(context),
            R.layout.nim_layout_message_bottom,
            this,
            true
        )
    }


    companion object {
        //默认状态 无键盘
        const val TYPE_DEFAULT = 0

        //输入状态 有键盘
        const val TYPE_INPUT = 1

        //显示emoji状态 无键盘
        const val TYPE_EMOJI = 2

        //显示更多 无键盘
        const val TYPE_MORE = 3

        //显示语言 无键盘
        const val TYPE_VOICE = 4
    }


    init {
        initView()
        initListener()
    }

    /**
     * 初始化各种事件
     */
    private fun initListener() {

        //设置点击事件
        setOnClickListener(
            this, binding.startVoice, binding.startKeyboard, binding.centerEmoji,
            binding.centerKeyboard, binding.endMore, binding.endSend
        )

        //中间输入框触摸事件
        binding.centerInput.touches {
            if (MotionEvent.ACTION_DOWN == it.action) {
                setInputType(TYPE_INPUT)
            }
            false
        }.subscribe {

        }

        //中间输入框文本改变事件
        binding.centerInput.addTextChangedListener(object : TextWatcher {
            private var start = 0
            private var count = 0
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                this.start = start
                this.count = count
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                binding.centerInput.removeTextChangedListener(this)
                checkSendButtonEnable()
                ToolMoon.replaceEmoticons(context, binding.centerInput, start, count)
                binding.centerInput.addTextChangedListener(this)
            }
        })

        //中间按住说话触摸事件
        binding.centerAudio.touches {
            when (it.action) {
                MotionEvent.ACTION_UP and MotionEvent.ACTION_CANCEL -> {
                    stopRecord(isCancelled(binding.centerAudio, it))
                }
                MotionEvent.ACTION_DOWN -> {
                    startRecord()
                }
                MotionEvent.ACTION_MOVE -> {
                    updateCancel(isCancelled(binding.centerAudio, it))
                }
            }
            false
        }.subscribe {

        }
    }

    /**
     * 更新cancelled值
     */
    private fun updateCancel(cancelled: Boolean) {
        if (!isOpenRecord)
            return
        if (this.cancelled == cancelled)
            return
        this.cancelled = cancelled
        onAudioUpdateCancelFunc?.invoke(this.cancelled)
    }

    /**
     * 判断是否取消
     */
    private fun isCancelled(view: View, event: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        return event.rawX < location[0] || event.rawX > location[0] + view.width || event.rawY < location[1] - 40
    }

    /**
     * 开始录音
     */
    private fun startRecord() {
        releaseDisposable()
        setAudioPressedColor()
        recordSecond = 0
        isOpenRecord = true
        cancelled = false
        onStartRecordFunc?.invoke()
        onAudioUpdateCancelFunc?.invoke(this.cancelled)
        disposable = Flowable.interval(0, 1, TimeUnit.SECONDS)
            .bindToSchedulers()
            .life(this)
            .subscribe {
                if (isOpenRecord) {
                    onAudioRecordChangeFunc?.invoke(recordSecond++)
                }
            }
    }

    /**
     * 停止录音
     */
    fun stopRecord(isCancelled: Boolean) {
        releaseDisposable()
        setAudioNormalColor()
        recordSecond = 0
        isOpenRecord = false
        if (isCancelled) {
            onAudioCancelFunc?.invoke()
        } else {
            onAudioSendFunc?.invoke()
        }
    }

    /**
     * 释放资源
     */
    private fun releaseDisposable() {
        disposable?.dispose()
        disposable = null
    }

    /**
     * 初始化View
     */
    private fun initView() {
        binding.centerInput.inputType =
            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_MULTI_LINE
        setAudioNormalColor()
    }

    /**
     * Audio正常背景颜色
     */
    private fun setAudioNormalColor() {
        binding.centerLayout.setBackgroundResource(R.color.color_f0f2f4)
    }

    /**
     * Audio按压背景颜色
     */
    private fun setAudioPressedColor() {
        binding.centerLayout.setBackgroundResource(R.color.color_dddddd)
    }


    /**
     * 设置类型
     *  @param inputType
     */
    fun setInputType(inputType: Int) {
        when (inputType) {
            TYPE_DEFAULT -> {// 1969   160   771
                hideSoftKeyboard()
                showBottom(false)
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
            }
            TYPE_INPUT -> {
                showBottom(false)
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                showSoftKeyboard()
            }
            TYPE_EMOJI -> {
                hideSoftKeyboard()
                showView(binding.startVoice, binding.centerKeyboard, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerEmoji, binding.centerAudio)
                showBottom(true)
                onShowEmoticonFunc?.invoke(R.id.layout_bottom)

            }
            TYPE_MORE -> {
                if (this.inputType == TYPE_MORE) {
                    setInputType(TYPE_INPUT)
                    return
                }
                hideSoftKeyboard()
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
                showBottom(true)
                onShowOperationFunc?.invoke(R.id.layout_bottom)
            }
            TYPE_VOICE -> {
                hideSoftKeyboard()
                showBottom(false)
                showView(binding.startKeyboard, binding.centerEmoji, binding.centerAudio)
                hideView(binding.startVoice, binding.centerKeyboard, binding.centerInput)
            }
        }
        checkSendButtonEnable()
        this.inputType = inputType
    }

    private var isSendShown = false

    /**
     * 更新输入判断是否显示发送
     */
    private fun checkSendButtonEnable() {
        val isShown = binding.centerInput.text.toString()
            .isNotEmpty() && binding.startVoice.visibility == View.VISIBLE
        if (isSendShown == isShown)
            return
        if (isShown) {
            binding.endSend.changeWidth(0f, 60f)
            alpha(binding.endMore, 300, 0, DEFAULT_INTERPOLATOR, 1f, 0f)
        } else {
            binding.endSend.changeWidth(60f, 0f)
            alpha(binding.endMore, 300, 0, DEFAULT_INTERPOLATOR, 0f, 1f)
        }
        isSendShown = isShown
    }

    /**
     * 点击事件
     */
    override fun onClick(view: View) {
        when (view) {
            binding.startVoice -> {
                setInputType(TYPE_VOICE)
            }
            binding.startKeyboard -> {
                setInputType(TYPE_INPUT)
            }
            binding.centerEmoji -> {
                setInputType(TYPE_EMOJI)
            }
            binding.centerKeyboard -> {
                setInputType(TYPE_INPUT)
            }
            binding.endMore -> {
                setInputType(TYPE_MORE)
            }
            binding.endSend -> {
                binding.centerInput.setText("")
            }
        }
    }

    private fun hideSoftKeyboard() {
        closeKeyboard(binding.centerInput)
    }

    /**
     * 弹出软键盘
     */
    private fun showSoftKeyboard() {
        binding.centerInput.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
        MainHandler.postDelayed(duration) { showKeyboard(context, binding.centerInput) }
    }

    /**
     * @param isShown 是否显示底部页面
     */
    private fun showBottom(isShown: Boolean) {
        if (isShown) {
            MainHandler.postDelayed(duration) {
                showView(binding.layoutBottom)
            }
        } else {
            hideView(binding.layoutBottom)
        }
    }

    /**
     * 初始化View
     */
    fun attachContentView(contentView: View, recyclerView: RecyclerView) {
        this.contentView = contentView
        this.recyclerView = recyclerView
    }

    /**
     * 获取底部显示的状态类型
     */
    fun getInputType(): Int {
        return inputType
    }


}