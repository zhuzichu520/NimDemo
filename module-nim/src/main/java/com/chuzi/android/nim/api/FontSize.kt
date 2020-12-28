package com.chuzi.android.nim.api

data class FontSize(
    /**
     * 等级
     */
    val level: Int,
    /**
     * 字体大小 单位sp
     */
    val size: Float
) {
    companion object {
        val arrays = arrayListOf(
            FontSize(0, 16f),
            FontSize(1, 18f),
            FontSize(2, 21f),
            FontSize(3, 24f),
            FontSize(4, 28f),
            FontSize(5, 32f)
        )
    }
}