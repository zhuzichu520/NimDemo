package com.chuzi.android.nim.emoji

/**
 * desc 贴图Item
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class StickerItem(//类别名
    val category: String,
    val name: String
) {

    val identifier: String get() = "$category/$name"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StickerItem

        if (category != other.category) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = category.hashCode()
        result = 31 * result + name.hashCode()
        return result
    }

}