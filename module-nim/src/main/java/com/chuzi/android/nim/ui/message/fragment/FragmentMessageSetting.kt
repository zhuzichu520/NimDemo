package com.chuzi.android.nim.ui.message.fragment


import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimFragmentMessageBinding
import com.chuzi.android.nim.ui.message.viewmodel.ViewModelMessageSetting
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/5 2:32 PM
 * since: v 1.0.0
 */

@Route(path = RoutePath.Nim.FRAGMENT_NIM_MESSAGE_SETTING)
class FragmentMessageSetting :
    FragmentBase<NimFragmentMessageBinding, ViewModelMessageSetting, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_message_setting

}