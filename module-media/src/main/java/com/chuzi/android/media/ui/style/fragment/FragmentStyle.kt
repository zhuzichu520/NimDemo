package com.chuzi.android.media.ui.style.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.media.R
import com.chuzi.android.media.BR
import com.chuzi.android.media.databinding.MediaFragmentStyleBinding
import com.chuzi.android.media.ui.style.viewmodel.ViewModelStyle
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/14 2:19 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Media.FRAGMENT_MEDIA_STYLE)
class FragmentStyle : FragmentBase<MediaFragmentStyleBinding, ViewModelStyle, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.media_fragment_style

}