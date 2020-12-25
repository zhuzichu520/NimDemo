package com.chuzi.android.nim.ui.message.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.alpha
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentEmoticonEmojiBinding
import com.chuzi.android.nim.emoji.EmojiManager
import com.chuzi.android.nim.ui.message.viewmodel.ItemViewModelEmoji
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelEmoticonEmoji
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc Emoji
 * author: 朱子楚
 * time: 2020/12/7 3:25 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_EMOTICON_EMOJI)
class FragmentMessageEmoticonEmoji :
    FragmentBase<NimFragmentEmoticonEmojiBinding, ViewModelEmoticonEmoji, ArgDefault>() {

    override fun setLayoutId(): Int = R.layout.nim_fragment_emoticon_emoji

    override fun bindVariableId(): Int = BR.viewModel

    override fun initData() {
        super.initData()
        MainHandler.postDelayed {
            loadData()
        }
    }

    fun loadData() {
        val list = mutableListOf<ItemViewModelEmoji>()
        for (i in 0 until EmojiManager.displayCount) {
            list.add(ItemViewModelEmoji(viewModel, i))
        }
        viewModel.items.value = list
        alpha(binding.content, 300, f = floatArrayOf(0f, 1f))
    }
}