package com.chuzi.android.nim.emoji

import com.chuzi.android.libs.tool.hasExtentsion
import com.chuzi.android.shared.global.AppGlobal.context
import java.util.*

/**
 * 贴图管理类
 */
object StickerManager {

    private const val CATEGORY_AJMD = "ajmd"
    private const val CATEGORY_XXY = "xxy"
    private const val CATEGORY_LT = "lt"

    init {
        initStickerOrder()
        loadStickerCategory()
    }

    /**
     * 数据源
     */
    private val stickerCategories: MutableList<StickerCategory> = ArrayList()
    private val stickerCategoryMap: MutableMap<String, StickerCategory> = HashMap()
    private val stickerOrder: MutableMap<String, Int> = HashMap(3)


    private fun initStickerOrder() {
        // 默认贴图顺序
        stickerOrder[CATEGORY_AJMD] = 1
        stickerOrder[CATEGORY_XXY] = 2
        stickerOrder[CATEGORY_LT] = 3
    }

    private fun isSystemSticker(category: String): Boolean {
        return CATEGORY_XXY == category || CATEGORY_AJMD == category || CATEGORY_LT == category
    }

    private fun getStickerOrder(categoryName: String): Int {
        return if (stickerOrder.containsKey(categoryName)) {
            stickerOrder[categoryName] ?: 0
        } else {
            100
        }
    }

    private fun loadStickerCategory() {
        val assetManager = context.resources.assets
        assetManager.list("sticker")?.let {
            var category: StickerCategory
            for (name in it) {
                if (!hasExtentsion(name)) {
                    category = StickerCategory(name, name, true, getStickerOrder(name))
                    stickerCategories.add(category)
                    stickerCategoryMap[name] = category
                }
            }
            stickerCategories.sortWith(Comparator { l, r -> l.order - r.order })
        }
    }

    @get:Synchronized
    val categories: List<StickerCategory>
        get() = stickerCategories

    @Synchronized
    fun getCategory(name: String): StickerCategory? {
        return stickerCategoryMap[name]
    }

    fun getStickerUri(categoryName: String, name: String): String? {
        var stickerName = name
        val category = getCategory(categoryName) ?: return null
        if (isSystemSticker(categoryName)) {
            if (!stickerName.contains(".png")) {
                stickerName += ".png"
            }
            val path = "sticker/" + category.name + "/" + stickerName
            return "file:///android_asset/$path"
        }
        return null
    }

}