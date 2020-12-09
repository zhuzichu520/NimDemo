package com.chuzi.android.nim.ui.me.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentMeBinding
import com.chuzi.android.nim.ui.me.viewmodel.ViewModelMe
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_ME)
class FragmentMe : FragmentBase<NimFragmentMeBinding, ViewModelMe, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_me

    override fun initLazyData() {
        super.initLazyData()
        viewModel.loadData()
    }

}