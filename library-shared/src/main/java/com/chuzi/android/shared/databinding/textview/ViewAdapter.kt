package com.chuzi.android.shared.databinding.textview

import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 4:41 PM
 * since: v 1.0.0
 */
@BindingAdapter(value = ["textId"], requireAll = false)
fun bindTextView(
    textView: TextView,
    @StringRes textId: Int?
) {
    textId?.let {
        textView.setText(textId)
    }
}
