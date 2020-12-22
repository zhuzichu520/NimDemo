package com.chuzi.android.nim.ui.contract.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.ext.toPostcard
import com.chuzi.android.mvvm.ext.withArg
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentTeamGroupsBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ViewModelTeamGroups
import com.chuzi.android.shared.base.DefaultIntFragmentPagerAdapter
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgGroupList
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 群组
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_CONTRACT_TEAMGROUPS)
class FragmentTeamGroups :
    FragmentBase<NimFragmentTeamGroupsBinding, ViewModelTeamGroups, ArgDefault>() {

    private val titles = listOf(
        R.string.team_group_my_created,
        R.string.team_group_my_joined
    )

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_team_groups

    override fun initView() {
        super.initView()
        initTabAndViewPager()
    }

    private fun initTabAndViewPager() {

        val builder = binding.tab.tabBuilder()

        repeat(titles.size) {
            binding.tab.addTab(builder.build(context))
        }

        val postcards = listOf(
            RoutePath.Nim.FRAGMENT_NIM_CONTRACT_TEAMGROUPS_LIST.toPostcard()
                .withArg(ArgGroupList(true)),
            RoutePath.Nim.FRAGMENT_NIM_CONTRACT_TEAMGROUPS_LIST.toPostcard()
                .withArg(ArgGroupList(false)),
        )

        binding.content.adapter = DefaultIntFragmentPagerAdapter(
            titles = titles,
            list = postcards,
            fm = childFragmentManager
        )

        binding.tab.setupWithViewPager(binding.content, true)
    }

}