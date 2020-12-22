package com.chuzi.android.shared.databinding.viewpager

import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

/**
 * desc
 * author: 朱子楚
 * time: 2020/4/22 5:13 PM
 * since: v 1.0.0
 */
@BindingAdapter("currentIndex")
fun currentIndex(viewPager: ViewPager, index: Int?) {
    index?.let {
        viewPager.setCurrentItem(it, false)
    }
}

@InverseBindingAdapter(attribute = "currentIndex", event = "OnPageChangeCallback")
fun getCurrentIndex(viewPager: ViewPager): Int {
    return viewPager.currentItem
}

@BindingAdapter("OnPageChangeCallback")
fun setOnPageChangeCallback(
    viewPager: ViewPager,
    bindingListener: InverseBindingListener?
) {
    bindingListener?.let {
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                if (state == ViewPager.SCROLL_STATE_IDLE)
                    bindingListener.onChange()
            }

        })
    }
}