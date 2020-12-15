package com.chuzi.android.shared.databinding.view

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.view.forEachIndexed
import androidx.databinding.BindingAdapter
import com.chuzi.android.mvvm.databinding.BindingCommand
import com.jakewharton.rxbinding4.view.clicks
import com.jakewharton.rxbinding4.view.longClicks
import com.jakewharton.rxbinding4.view.touches
import io.reactivex.rxjava3.core.Observable
import java.util.concurrent.TimeUnit

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/15 4:29 PM
 * since: v 1.0.0
 */

private fun <T> Observable<T>.isThrottleFirst(
    isThrottleFirst: Boolean
): Observable<T> {
    return this.compose {
        if (isThrottleFirst) {
            it.throttleFirst(150L, TimeUnit.MILLISECONDS)
        } else {
            it
        }
    }
}

@BindingAdapter(
    value =
    [
        "onClickCommand",
        "onLongClickCommand",
        "onClickViewCommand",
        "onLongClickViewCommand",
        "onTouchCommmand",
        "isThrottleFirst"
    ],
    requireAll = false
)
fun onClickCommand(
    view: View,
    clickCommand: BindingCommand<*>?,
    longClickCommand: BindingCommand<*>?,
    onClickViewCommand: BindingCommand<*>?,
    onLongClickViewCommand: BindingCommand<*>?,
    onTouchCommmand: BindingCommand<*>?,
    isThrottleFirst: Boolean?
) {
    val isThrottle = isThrottleFirst ?: false

    clickCommand?.apply {
        view.clicks().isThrottleFirst(isThrottle).subscribe {
            execute()
        }
    }

    longClickCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottle).subscribe {
            execute()
        }
    }

    onClickViewCommand?.apply {
        view.clicks().isThrottleFirst(isThrottle).subscribe {
            execute(view)
        }
    }

    onLongClickViewCommand?.apply {
        view.longClicks().isThrottleFirst(isThrottle).subscribe {
            execute(view)
        }
    }

    onTouchCommmand?.apply {
        view.touches {
            execute(it)
            false
        }.subscribe {

        }
    }
}

@BindingAdapter(value = ["visibility", "isShown"], requireAll = false)
fun bindViewVisibility(view: View, visibility: Int?, isShown: Boolean?) {
    visibility?.let {
        view.visibility = visibility
    }
    isShown?.let {
        if (isShown) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }
}

/**
 * 设置控件Click监听事件
 *
 * @param onClickListener 监听实例
 * @param views           视图集合
 */
fun setOnClickDoubleListener(
    onClickListener: View.OnClickListener?,
    vararg views: View?,
    isThrottleFirst: Boolean? = null
) {
    if (views.isNotEmpty() && onClickListener != null) {
        for (view in views) {
            view?.let { v ->
                v.clicks().isThrottleFirst(isThrottleFirst ?: true).subscribe {
                    onClickListener.onClick(v)
                }
            }
        }
    }
}


@BindingAdapter(value = ["backgroundId"], requireAll = false)
fun bindViewColor(view: View, @DrawableRes backgroundId: Int?) {
    backgroundId?.let {
        view.setBackgroundResource(it)
    }
}

@BindingAdapter(value = ["displayChild", "displayChild2", "layoutDirection"], requireAll = false)
fun bindViewGroup(
    viewGroup: ViewGroup,
    displayChild: Int?,
    displayChild2: Int?,
    layoutDirection: Int?
) {
    displayChild?.let {
        viewGroup.forEachIndexed { index, view ->
            if (it == index) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }

    displayChild2?.let {
        viewGroup.forEachIndexed { index, view ->
            if (it == index) {
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.INVISIBLE
            }
        }
    }

    layoutDirection?.let {
        viewGroup.layoutDirection = layoutDirection
    }
}