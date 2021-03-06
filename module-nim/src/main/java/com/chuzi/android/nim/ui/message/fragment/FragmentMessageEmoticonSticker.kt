package com.chuzi.android.nim.ui.message.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.alpha
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimFragmentEmoticonStickerBinding
import com.chuzi.android.nim.entity.arg.ArgSticker
import com.chuzi.android.nim.ui.message.viewmodel.ItemViewModelSticker
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelEmoticonSticker
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 贴图
 * author: 朱子楚
 * time: 2020/12/7 3:25 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_EMOTICON_STICKER)
class FragmentMessageEmoticonSticker :
    FragmentBase<NimFragmentEmoticonStickerBinding, ViewModelEmoticonSticker, ArgSticker>() {

    override fun setLayoutId(): Int = R.layout.nim_fragment_emoticon_sticker

    override fun bindVariableId(): Int = BR.viewModel

    override fun initData() {
        super.initData()
        MainHandler.postDelayed {
            alpha(binding.content, 300, f = floatArrayOf(0f, 1f))
            loadData()
        }
    }

    fun loadData() {
        val stickers = arg.stickerCategory.stickers
        viewModel.items.value = stickers?.map {
            ItemViewModelSticker(viewModel, it)
        }
    }

}