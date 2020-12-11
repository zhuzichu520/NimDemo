package com.chuzi.android.nim.databinding

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.chuzi.android.mvvm.databinding.BindingCommand
import com.chuzi.android.nim.emoji.ToolMoon
import com.chuzi.android.nim.view.LayoutBadge
import com.chuzi.android.widget.badge.Badge

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/10 6:25 PM
 * since: v 1.0.0
 */

@BindingAdapter(value = ["moonText"], requireAll = false)
fun bindMoonTextView(
    textView: TextView,
    moonText: String?
) {
    moonText?.let {
        ToolMoon.identifyFaceExpression(textView.context, textView, it)
    }
}

@BindingAdapter("number", "onDragStateChangedCommamnd")
fun bindViewSessionBadge(
    viewSessionBadge: LayoutBadge,
    number: Int?,
    onDragStateChangedCommamnd: BindingCommand<Int>?
) {
    number?.let {
        viewSessionBadge.number = it
    }
    onDragStateChangedCommamnd?.let {
        viewSessionBadge.onDragStateChangedListener =
            Badge.OnDragStateChangedListener { dragState, _, _ ->
                onDragStateChangedCommamnd.execute(dragState)
            }
    }
}