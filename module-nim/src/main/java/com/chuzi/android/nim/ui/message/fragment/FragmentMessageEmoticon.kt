package com.chuzi.android.nim.ui.message.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentMessageEmoticonBinding
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessageEmoticon
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

    override fun initViewObservable() {
        super.initViewObservable()
        viewModel.tabIndex.observe(viewLifecycleOwner, {
            viewModel.updateTabs()
        })
    }

    override fun initData() {
        super.initData()
        viewModel.tabIndex.value = 0
    }

}