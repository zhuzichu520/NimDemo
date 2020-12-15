package com.chuzi.android.shared.databinding.qmui

import android.view.View
import androidx.databinding.BindingAdapter
import com.chuzi.android.mvvm.databinding.BindingCommand
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.qmuiteam.qmui.widget.QMUITopBarLayout
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton
import com.chuzi.android.shared.R
import com.chuzi.android.shared.entity.enumeration.EnumEmptyStatus
import com.chuzi.android.shared.ext.toStringByResId
import com.qmuiteam.qmui.widget.pullLayout.QMUIPullLayout
import com.qmuiteam.qmui.widget.textview.QMUILinkTextView

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/15 1:52 PM
 * since: v 1.0.0
 */
@BindingAdapter(value = ["qmuiTopTitle"], requireAll = false)
fun bindQMUITopBarLayout(
    topBarLayout: QMUITopBarLayout,
    title: String?
) {
    title?.let {
        topBarLayout.setTitle(it)
    }
}

@BindingAdapter(value = ["qmuiAlphaWhenPress"], requireAll = false)
fun bindQMUIRoundButton(
    roundButton: QMUIRoundButton,
    qmuiAlphaWhenPress: Boolean?
) {
    qmuiAlphaWhenPress?.let {
        roundButton.setChangeAlphaWhenPress(qmuiAlphaWhenPress == true)
    }
}

@BindingAdapter(value = ["emptyStatus", "emptyErrorCommand"], requireAll = false)
fun bindQMUIEmptyView(
    emptyView: QMUIEmptyView,
    emptyStatus: EnumEmptyStatus?,
    onErrorCommand: BindingCommand<*>?
) {
    when (emptyStatus) {
        EnumEmptyStatus.SUCCESS -> {
            emptyView.hide()
        }
        EnumEmptyStatus.ERROR -> {
            emptyView.show(
                false,
                R.string.empty_desc_fail_title.toStringByResId(emptyView.context),
                R.string.empty_desc_fail_desc.toStringByResId(emptyView.context),
                R.string.empty_desc_retry.toStringByResId(emptyView.context)
            ) {
                onErrorCommand?.execute()
            }
        }
        EnumEmptyStatus.LOADING -> {
            emptyView.show(true)
        }
        EnumEmptyStatus.EMPTY -> {
            emptyView.show(
                R.string.empty_desc_title.toStringByResId(emptyView.context),
                null
            )
        }
        else -> {
            emptyView.hide()
        }
    }
}

@BindingAdapter(
    value = ["onClickLinkTelCommand", "onClickLinkEmailConmmnd", "onClickLinkUrlCommand", "onLongClickLinkCommand"],
    requireAll = false
)
fun bindingQmuiTextView(
    qmuiLinkTextView: QMUILinkTextView,
    onClickTelCommand: BindingCommand<String>?,
    onClickEmailConmmnd: BindingCommand<String>?,
    onClickUrlCommand: BindingCommand<String>?,
    onLongClickLinkCommand: BindingCommand<View>?
) {

    qmuiLinkTextView.setOnLinkClickListener(object : QMUILinkTextView.OnLinkClickListener {

        override fun onMailLinkClick(mailAddress: String?) {
            onClickEmailConmmnd?.execute(mailAddress)
        }

        override fun onWebUrlLinkClick(url: String?) {
            onClickUrlCommand?.execute(url)
        }

        override fun onTelLinkClick(phoneNumber: String?) {
            onClickTelCommand?.execute(phoneNumber)
        }

    })

    qmuiLinkTextView.setOnLinkLongClickListener {
        onLongClickLinkCommand?.execute(qmuiLinkTextView)
    }

}

@BindingAdapter(value = ["onRefreshCommand", "onLoadMoreCommand"], requireAll = false)
fun bindQMUIPullLayout(
    qmuiPullLayout: QMUIPullLayout,
    onRefreshCommand: BindingCommand<QMUIAction>?,
    onLoadMoreCommand: BindingCommand<QMUIAction>?
) {
    qmuiPullLayout.setActionListener {
        when (it.pullEdge) {
            QMUIPullLayout.PULL_EDGE_TOP -> {
                onRefreshCommand?.execute(QMUIAction(qmuiPullLayout, it))
            }
            QMUIPullLayout.PULL_EDGE_BOTTOM -> {
                onLoadMoreCommand?.execute(QMUIAction(qmuiPullLayout, it))
            }
        }
    }
}

class QMUIAction(
    private val pullLayout: QMUIPullLayout,
    private val action: QMUIPullLayout.PullAction
) {
    fun finish() {
        pullLayout.finishActionRun(action)
    }
}