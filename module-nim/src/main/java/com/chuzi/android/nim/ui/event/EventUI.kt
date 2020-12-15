package com.chuzi.android.nim.ui.event

import com.chuzi.android.nim.emoji.StickerItem

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:47 PM
 * since: v 1.0.0
 */
class EventUI {

    /**
     * 消息界面回退事件
     */
    class OnMessageDestoryEvent

    /**
     * 点击Emoji删除按钮
     */
    class OnClickEmojiDeleteEvent

    /**
     * 点击Emoji事件
     */
    data class OnClickEmojiEvent(
        val text: String
    )

    /**
     * 点击贴图事件
     */
    class OnClickStickerEvent(
        val sticker: StickerItem
    )

    /**
     * 点击底部更多Item事件
     */
    class OnClickOperationEvent(
        val titleId: Int
    )
}