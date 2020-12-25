package com.chuzi.android.shared.databinding.imageview

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import com.bumptech.glide.request.RequestOptions
import com.chuzi.android.shared.tools.GlideApp


/**
 * desc
 * author: 朱子楚
 * time: 2020/9/18 1:12 PM
 * since: v 1.0.0
 */
@BindingAdapter(value = ["url", "placeholder"], requireAll = false)
fun bindImageView(
    imageView: ImageView,
    url: Any?,
    @DrawableRes placeholder: Int?
) {
    when (url) {
        null -> {
            placeholder?.let { imageView.setImageResource(it) }
        }
        is Drawable -> {
            imageView.setImageDrawable(url)
        }
        is Int -> {
            imageView.setImageResource(url)
        }
        else -> {
            val requestOptions = RequestOptions()
            val glide = GlideApp.with(imageView).load(url)
            placeholder?.let {
                requestOptions.placeholder(it)
                requestOptions.error(it)
            }
            glide.apply(requestOptions)
            glide.into(imageView)
        }
    }


}

@BindingAdapter(value = ["isGray"], requireAll = false)
fun bindImageView2(
    imageView: ImageView,
    isGray: Boolean?
) {
    isGray?.let {
        if (it) {
            val cm = ColorMatrix()
            cm.setSaturation(0f) // 设置饱和度
            val grayColorFilter = ColorMatrixColorFilter(cm)
            imageView.colorFilter = grayColorFilter
        } else {
            imageView.colorFilter = null
        }

    }
}

