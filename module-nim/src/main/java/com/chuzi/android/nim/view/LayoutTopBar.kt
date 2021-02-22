package com.chuzi.android.nim.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.chuzi.android.libs.tool.getStatusBarHeight
import com.chuzi.android.mvvm.databinding.BindingCommand
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimLayoutTopBarBinding
import com.chuzi.android.shared.databinding.view.setOnClickDoubleListener
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * desc 统一页面头部
 * author: 朱子楚
 * time: 2020/12/10 2:41 PM
 * since: v 1.0.0
 */
class LayoutTopBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnClickListener {

    /**
     * 标题
     */
    var topTitle: Any? = null
        set(value) {
            (value as? String)?.let {
                binding.title.text = it
            }
            (value as? Int)?.let {
                binding.title.setText(it)
            }
            field = value
        }

    /**
     * 返回点击事件
     */
    var onClickBackCommand: BindingCommand<Any>? = null

    /**
     * 绑定DataBinding
     */
    private val binding by lazy {
        createCommand { }
        DataBindingUtil.inflate<NimLayoutTopBarBinding>(
            LayoutInflater.from(context),
            R.layout.nim_layout_top_bar,
            this,
            true
        )
    }

    /**
     * 初始化
     */
    init {
        binding.root.setPadding(0, getStatusBarHeight(context), 0, 0)
        setOnClickDoubleListener(this, binding.layoutBack)
    }

    /**
     * 处理点击事件
     */
    override fun onClick(view: View?) {
        when (view) {
            binding.layoutBack -> {
                onClickBackCommand?.execute()
            }
            else -> {
            }
        }
    }

}