package com.chuzi.android.shared.databinding.imageview

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
    url?.apply {
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
