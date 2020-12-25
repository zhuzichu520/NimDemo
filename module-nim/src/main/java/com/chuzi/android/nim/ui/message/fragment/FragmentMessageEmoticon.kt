package com.chuzi.android.nim.ui.message.fragment

import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.ext.toPostcard
import com.chuzi.android.mvvm.ext.withArg
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentMessageEmoticonBinding
import com.chuzi.android.nim.emoji.StickerManager
import com.chuzi.android.nim.entity.arg.ArgSticker
import com.chuzi.android.nim.ui.message.viewmodel.ItemViewModelEmoticonTab
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessageEmoticon
import com.chuzi.android.shared.base.DefaultIntFragmentPagerAdapter
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/7 3:25 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_EMOTICON)
class FragmentMessageEmoticon :
    FragmentBase<NimFragmentMessageEmoticonBinding, ViewModelMessageEmoticon, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_message_emoticon


    override fun initData() {
        super.initData()
        loadData()
    }

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.tabIndex.observe(viewLifecycleOwner, {
            viewModel.updateTabs()
        })
    }

    fun loadData() {
        val tabs = arrayListOf<Any>()
        val postcards = arrayListOf<Postcard>()
        var i = 0
        tabs.add(
            ItemViewModelEmoticonTab(
                viewModel,
                R.mipmap.nim_ic_emoji_normal,
                R.mipmap.nim_ic_emoji_pressed,
                i++
            )
        )
        postcards.add(RoutePath.Nim.FRAGMENT_NIM_MESSAGE_EMOTICON_EMOJI.toPostcard())
        val categories = StickerManager.categories
        categories.forEach { item ->
            tabs.add(
                ItemViewModelEmoticonTab(
                    viewModel,
                    item.normalUri,
                    item.pressedlUri,
                    i++
                )
            )
            postcards.add(
                RoutePath.Nim.FRAGMENT_NIM_MESSAGE_EMOTICON_STICKER.toPostcard()
                    .withArg(ArgSticker(item))
            )
        }
        binding.pager.offscreenPageLimit = postcards.size
        binding.pager.adapter = DefaultIntFragmentPagerAdapter(parentFragmentManager, postcards)
        viewModel.items.value = tabs
        viewModel.tabIndex.value = 0
    }
}