package com.chuzi.android.nim.view

import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
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
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout
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
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 根布局
     */
    private lateinit var contentView: View

    /**
     * 动画时间
     */
    private val duration = 150L

    /**
     * 类型
     */
    private var inputType: Int = TYPE_DEFAULT

    /**
     * 消息列表的RecyclerView
     */
    private lateinit var recyclerView: RecyclerView

    /**
     * recyclerview父布局
     */
    private lateinit var pullLayout: QMUIPullLayout

    /**
     * contentView 的高度，当键盘弹起来，高度会变
     */
    private var contentViewHeight: Int = 0

    private var softKeyboardHeight: Int? = null

    /**
     * 底部emoji页面高度
     */
    private val emojiHeight = dp2px(context, 380f)

    /**
     * 底部更多页面高度
     */
    private val operationHeight = dp2px(context, 310f)

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
     * 文本发送
     */
    var onTextSendFunc: ((String) -> Unit)? = null

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

    private fun initKeyboardListener() {
        val screenHeight = context.resources.displayMetrics.heightPixels
        val screenHeight6 = screenHeight / 6
        val rootView = (context as Activity).window.decorView
        var virtualKeyboardHeight = 0
        var isKeyboardShow = false
        rootView.viewTreeObserver.addOnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val heightDifference = screenHeight - rect.bottom
            if (heightDifference < screenHeight6) {
                virtualKeyboardHeight = heightDifference
                if (isKeyboardShow) {
                    isKeyboardShow = false
                }
            } else {
                val softKeyboardHeight = heightDifference - virtualKeyboardHeight
                if (!isKeyboardShow) {
                    isKeyboardShow = true
                    this.softKeyboardHeight = softKeyboardHeight
                }
                //切换键盘高度发生变化
                if (this.softKeyboardHeight != softKeyboardHeight) {
                    this.softKeyboardHeight = softKeyboardHeight
                }
            }
        }
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
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
                lockRecyclerViewHeight(contentViewHeight - getInputHeight())
                hideSoftKeyboard()
                showBottom(false)
                unlockRecyclerViewHeight()
            }
            TYPE_INPUT -> {
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                softKeyboardHeight?.let {
                    lockRecyclerViewHeight(contentViewHeight - it - getInputHeight())
                }
                showBottom(false)
                showSoftKeyboard()
                softKeyboardHeight?.let {
                    unlockRecyclerViewHeight()
                }
            }
            TYPE_EMOJI -> {
                showView(binding.startVoice, binding.centerKeyboard, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerEmoji, binding.centerAudio)
                lockRecyclerViewHeight(contentViewHeight - emojiHeight - getInputHeight())
                onShowEmoticonFunc?.invoke(R.id.layout_bottom)
                showBottom(true)
                hideSoftKeyboard()
                unlockRecyclerViewHeight()
            }
            TYPE_MORE -> {
                if (this.inputType == TYPE_MORE) {
                    setInputType(TYPE_INPUT)
                    return
                }
                showView(binding.startVoice, binding.centerEmoji, binding.centerInput)
                hideView(binding.startKeyboard, binding.centerKeyboard, binding.centerAudio)
                lockRecyclerViewHeight(contentViewHeight - operationHeight - getInputHeight())
                onShowOperationFunc?.invoke(R.id.layout_bottom)
                hideSoftKeyboard()
                showBottom(true)
                unlockRecyclerViewHeight()
            }
            TYPE_VOICE -> {
                showView(binding.startKeyboard, binding.centerEmoji, binding.centerAudio)
                hideView(binding.startVoice, binding.centerKeyboard, binding.centerInput)
                lockRecyclerViewHeight(contentViewHeight - getInputHeight())
                hideSoftKeyboard()
                showBottom(false)
                unlockRecyclerViewHeight()
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
        val isShown =
            !binding.centerInput.text.isNullOrBlank() && binding.startVoice.visibility == View.VISIBLE
        if (isSendShown == isShown)
            return
        if (isShown) {
            binding.endSend.changeWidth(0f, 60f, 100)
            alpha(binding.endMore, 100, 0, AccelerateInterpolator(), 1f, 0f)
        } else {
            binding.endSend.changeWidth(60f, 0f, 100)
            alpha(binding.endMore, 100, 0, AccelerateInterpolator(), 0f, 1f)
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
                onTextSendFunc?.invoke(binding.centerInput.text.toString())
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
        showKeyboard(context, binding.centerInput)
        MainHandler.postDelayed(duration) {
            scrollToBottom()
        }
    }

    /**
     * @param isShown 是否显示底部页面
     */
    private fun showBottom(isShown: Boolean) {
        if (isShown) {
            showView(binding.layoutBottom)
            scrollToBottom()
        } else {
            hideView(binding.layoutBottom)
        }
    }

    /**
     * 初始化View
     */
    fun attachContentView(
        contentView: View,
        recyclerView: RecyclerView,
        pullLayout: QMUIPullLayout
    ) {
        this.contentView = contentView
        this.recyclerView = recyclerView
        this.pullLayout = pullLayout
        this.contentViewHeight = contentView.height
        initKeyboardListener()
    }

    /**
     * 获取输入框界面高度
     */
    private fun getInputHeight(): Int {
        return binding.layoutInput.height
    }

    /**
     * 获取底部显示的状态类型
     */
    fun getInputType(): Int {
        return inputType
    }

    /**
     * 追加字符串
     */
    fun appendText(s: String?) {
        val text = binding.centerInput.text ?: return
        var start: Int = binding.centerInput.selectionStart
        var end: Int = binding.centerInput.selectionEnd
        start = if (start < 0) 0 else start
        end = if (start < 0) 0 else end
        text.replace(start, end, s)
    }

    /**
     * 删除text
     */
    fun dropText() {
        binding.centerInput.dispatchKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
    }

    /**
     * 获取输入框字符串
     */
    fun getText(): String? {
        val editable = binding.centerInput.text ?: return null
        return editable.toString()
    }

    /**
     * 输入框设置字符串
     */
    fun setText(text: String?) {
        binding.centerInput.setText(text)
    }

    /**
     * 滑动到置顶位置
     */
    private fun scrollToBottom() {
        recyclerView.scrollBy(0, Int.MAX_VALUE)
    }


    /**
     * 锁定RecyclerView的高度
     */
    private fun lockRecyclerViewHeight(height: Int) {
        val layoutParams = pullLayout.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        val valueAnimator = ValueAnimator.ofInt(pullLayout.height, height)
        valueAnimator.addUpdateListener {
            layoutParams.height = toInt(it.animatedValue)
            pullLayout.requestLayout()
            scrollToBottom()
        }
        valueAnimator.duration = duration
        valueAnimator.start()
    }

    /**
     * 释放锁定的RecyclerView的高度
     */
    private fun unlockRecyclerViewHeight() {
        MainHandler.postDelayed(200) {
            val layoutParams = pullLayout.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = 1f
        }
    }

}