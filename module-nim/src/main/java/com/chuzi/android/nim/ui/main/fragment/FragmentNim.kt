package com.chuzi.android.nim.ui.main.fragment

import android.graphics.Typeface
import android.view.Gravity
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.ext.toPostcard
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.databinding.NimFragmentNimBinding
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.ui.main.viewmodel.ViewModelNim
import com.chuzi.android.shared.base.DefaultIntFragmentPagerAdapter
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.ext.dp2px
import com.chuzi.android.shared.ext.toDrawableByResId
import com.chuzi.android.shared.ext.toStringByResId
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.widget.badge.Badge
import com.chuzi.android.widget.badge.QBadgeView
import com.qmuiteam.qmui.util.QMUIDisplayHelper
import com.qmuiteam.qmui.widget.tab.QMUITabBuilder

/**
 * desc NIM主页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_MAIN)
class FragmentNim : FragmentBase<NimFragmentNimBinding, ViewModelNim, ArgDefault>() {

    lateinit var badge: Badge

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_nim

    override fun initView() {
        super.initView()
        val postcards = listOf(
            RoutePath.Nim.FRAGMENT_NIM_SESSION.toPostcard(),
            RoutePath.Media.FRAGMENT_MEDIA_STYLE.toPostcard(),
            RoutePath.Nim.FRAGMENT_NIM_CONTRACT.toPostcard(),
            RoutePath.Nim.FRAGMENT_NIM_ME.toPostcard()
        )
        initTabs()
        initBadge()
        binding.pager.offscreenPageLimit = postcards.size
        binding.pager.adapter = DefaultIntFragmentPagerAdapter(parentFragmentManager, postcards)
        binding.tabs.setupWithViewPager(binding.pager, false)
    }

    private fun initBadge() {
        badge = QBadgeView(context).apply {
            isShowShadow = false
            setBadgeTextSize(13.5f, true)
        }.bindTarget(binding.tabs)
        badge.badgeGravity = Gravity.TOP or Gravity.START
        badge.setGravityOffset(58f, 0f, true)
        badge.setOnDragStateChangedListener { dragState, _, _ ->
            if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState) {
                msgService().clearAllUnreadCount()
            }
        }
    }

    override fun initViewObservable() {
        super.initViewObservable()

        AppFactorySDK.unReadNumber.observe(viewLifecycleOwner) {
            badge.badgeNumber = it
        }

    }


    /**
     * 初始化底部导航
     */
    private fun initTabs() {

        val builder: QMUITabBuilder = binding.tabs.tabBuilder()
        builder.setTypeface(null, Typeface.DEFAULT_BOLD)
        builder.setSelectedIconScale(1.2f)
            .setTextSize(
                QMUIDisplayHelper.sp2px(context, 13),
                QMUIDisplayHelper.sp2px(context, 15)
            )
            .skinChangeWithTintColor(true)
            .setDynamicChangeIconColor(false)
            .setNormalIconSizeInfo(24f.dp2px(), 24f.dp2px())

        val session = builder
            .setNormalDrawable(R.mipmap.nim_ic_main_session_normal.toDrawableByResId())
            .setSelectedDrawable(R.mipmap.nim_ic_main_session_selected.toDrawableByResId())
            .setText(R.string.session.toStringByResId(requireContext()))
            .build(context)

        val news = builder
            .setNormalDrawable(R.mipmap.nim_ic_main_news_normal.toDrawableByResId())
            .setSelectedDrawable(R.mipmap.nim_ic_main_news_selected.toDrawableByResId())
            .setText(R.string.news.toStringByResId(requireContext()))
            .build(context)

        val contract = builder
            .setNormalDrawable(R.mipmap.nim_ic_main_contract_normal.toDrawableByResId())
            .setSelectedDrawable(R.mipmap.nim_ic_main_contract_selected.toDrawableByResId())
            .setText(R.string.contract.toStringByResId(requireContext()))
            .build(context)

        val me = builder
            .setNormalDrawable(R.mipmap.nim_ic_main_me_normal.toDrawableByResId())
            .setSelectedDrawable(R.mipmap.nim_ic_main_me_selected.toDrawableByResId())
            .setText(R.string.me.toStringByResId(requireContext()))
            .build(context)

        binding.tabs.addTab(session).addTab(news).addTab(contract).addTab(me)

    }


}
