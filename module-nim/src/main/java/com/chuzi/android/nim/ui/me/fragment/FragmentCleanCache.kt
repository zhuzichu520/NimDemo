package com.chuzi.android.nim.ui.me.fragment

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentCleanCacheBinding
import com.chuzi.android.nim.ui.me.viewmodel.ViewModelCleanCache
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.databinding.view.setOnClickDoubleListener
import com.chuzi.android.shared.global.CacheGlobal
import com.chuzi.android.shared.route.RoutePath

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:47 AM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_ME_CLEAN_CACHE)
class FragmentCleanCache :
    FragmentBase<NimFragmentCleanCacheBinding, ViewModelCleanCache, ArgDefault>(),
    View.OnClickListener {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_clean_cache

    override fun initView() {
        super.initView()
        viewModel.updateCacheSize()
    }

    override fun initListener() {
        super.initListener()
        setOnClickDoubleListener(this, binding.clear)
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.clear -> {
                CacheGlobal.clear()
                viewModel.updateCacheSize()
            }
        }
    }

}