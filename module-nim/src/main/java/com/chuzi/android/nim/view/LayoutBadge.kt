package com.chuzi.android.nim.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.chuzi.android.nim.R
import com.chuzi.android.widget.badge.Badge
import com.chuzi.android.widget.badge.QBadgeView

/**
 * desc 获取最近会话列表Item的小红点
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class LayoutBadge @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var badge: Badge

    var onDragStateChangedListener: Badge.OnDragStateChangedListener? = null
        set(value) {
            field = value
            badge.setOnDragStateChangedListener(field)
        }

    var number: Int = 0
        set(value) {
            field = value
            badge.badgeNumber = field
        }

    init {
        LayoutInflater.from(context).inflate(R.layout.nim_layout_session_badge, this)
        badge = QBadgeView(context).apply {
            isShowShadow = false
            setBadgeTextSize(13.5f, true)
        }.bindTarget(findViewById(R.id.target))
        badge.badgeGravity = Gravity.TOP or Gravity.END
        badge.setGravityOffset(16f, 42f, true)
    }
}