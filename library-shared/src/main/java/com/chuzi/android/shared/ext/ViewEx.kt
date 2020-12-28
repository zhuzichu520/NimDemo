package com.chuzi.android.shared.ext

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.RecyclerView
import com.chuzi.android.libs.tool.dp2px
import com.chuzi.android.libs.tool.toInt
import com.qmuiteam.qmui.recyclerView.QMUIRVDraggableScrollBar

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/8 2:47 PM
 * since: v 1.0.0
 */
fun View.changeWidth(startWidth: Float, endWidth: Float, duration: Long = 300) {
    val layoutParams = this.layoutParams
    val valueAnimator =
        ValueAnimator.ofInt(dp2px(this.context, startWidth), dp2px(this.context, endWidth))
    valueAnimator.duration = duration
    valueAnimator.interpolator = AccelerateInterpolator()
    valueAnimator.addUpdateListener {
        layoutParams.width = toInt(it.animatedValue)
        this.layoutParams = layoutParams
    }
    valueAnimator.start()
}

fun View.changeHight(startHight: Float, endHight: Float, duration: Long = 300) {
    val layoutParams = this.layoutParams
    val valueAnimator =
        ValueAnimator.ofInt(dp2px(this.context, startHight), dp2px(this.context, endHight))
    valueAnimator.duration = duration
    valueAnimator.interpolator = AccelerateInterpolator()
    valueAnimator.addUpdateListener {
        layoutParams.height = toInt(it.animatedValue)
        this.layoutParams = layoutParams
    }
    valueAnimator.start()
}

fun RecyclerView.showScrollBar() {
    val scrollBar = QMUIRVDraggableScrollBar(0, 0, 0)
    scrollBar.isEnableScrollBarFadeInOut = true
    scrollBar.attachToRecyclerView(this)
}