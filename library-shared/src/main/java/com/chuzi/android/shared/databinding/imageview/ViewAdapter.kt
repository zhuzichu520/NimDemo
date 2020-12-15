package com.chuzi.android.shared.databinding.imageview

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter
import coil.load
import com.chuzi.android.mvvm.ext.className
import com.chuzi.android.shared.global.AppGlobal

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
    when (url) {
        is String -> {
            imageView.load(url, imageLoader = AppGlobal.imageLoader) {
                crossfade?.let {
                    crossfade(it)
                }
                placeholder?.let {
                    placeholder(it)
                    error(it)
                }
            }
        }
        is Uri -> {
            imageView.load(url, imageLoader = AppGlobal.imageLoader) {
                crossfade?.let {
                    crossfade(it)
                }
                placeholder?.let {
                    placeholder(it)
                    error(it)
                }
            }
        }
        is Int -> {
            imageView.setImageResource(url)
        }
        is Drawable -> {
            imageView.setImageDrawable(url)
        }
        is Bitmap -> {
            imageView.setImageBitmap(url)
        }
    }

}
