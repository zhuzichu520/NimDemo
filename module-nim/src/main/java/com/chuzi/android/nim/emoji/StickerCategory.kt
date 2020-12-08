package com.chuzi.android.nim.emoji

import android.content.Context
import android.net.Uri
import com.chuzi.android.shared.global.AppGlobal.context
import java.io.IOException
import java.io.InputStream
import java.io.Serializable
import java.util.*

class StickerCategory(// 贴纸包名
    var name: String, // 显示的标题
    var title: String, // 是否是系统内置表情
    var system: Boolean,
    var order: Int
) : Serializable {

    @Transient
    var stickers: List<StickerItem>? = null
        private set


    init {
        loadStickerData()
    }

    fun hasStickers(): Boolean = !stickers.isNullOrEmpty()

    val normalUri: Uri
        get() {
            val filename = name + "_s_normal.png"
            return Uri.parse("file:///android_asset/sticker/$filename")
        }

    val pressedlUri: Uri
        get() {
            val filename = name + "_s_pressed.png"
            return Uri.parse("file:///android_asset/sticker/$filename")
        }

    fun getCoverNormalInputStream(context: Context): InputStream? {
        val filename = name + "_s_normal.png"
        return makeFileInputStream(context, filename)
    }

    fun getCoverPressedInputStream(context: Context): InputStream? {
        val filename = name + "_s_pressed.png"
        return makeFileInputStream(context, filename)
    }

    val count: Int
        get() = if (stickers == null || stickers!!.isEmpty()) {
            0
        } else stickers!!.size

    private fun makeFileInputStream(context: Context, filename: String): InputStream? {
        try {
            if (system) {
                val assetManager = context.resources.assets
                val path = "sticker/$filename"
                return assetManager.open(path)
            } else {
                // for future
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun loadStickerData(): List<StickerItem> {
        val stickers: MutableList<StickerItem> = ArrayList()
        val assetManager = context.resources.assets
        try {
            val files = assetManager.list("sticker/$name")
            for (file in files!!) {
                stickers.add(StickerItem(name, file!!))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        this.stickers = stickers
        return stickers
    }

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is StickerCategory) {
            return false
        }
        if (other === this) {
            return true
        }
        return other.name == name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

}