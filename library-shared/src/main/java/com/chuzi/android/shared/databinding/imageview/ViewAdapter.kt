package com.chuzi.android.shared.databinding.imageview

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load

/**
 * desc
 * author: 朱子楚
 * time: 2020/9/18 1:12 PM
 * since: v 1.0.0
 */
@BindingAdapter(value = ["url", "crossfade", "placeholder"], requireAll = false)
fun bindImageView(
    imageView: ImageView,
    url: Any?,
    crossfade: Boolean?,
    @DrawableRes placeholder: Int?
) {
    (url as? String)?.let {
        imageView.load(url) {
            crossfade?.let {
                crossfade(it)
            }
            placeholder?.let {
                placeholder(it)
                error(it)
            }
        }
    }
    (url as? Int)?.let {
        imageView.setImageResource(it)
    }
}
