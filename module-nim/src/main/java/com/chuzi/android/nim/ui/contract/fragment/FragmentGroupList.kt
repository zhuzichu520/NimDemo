package com.chuzi.android.nim.ui.contract.fragment

import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.R
import com.chuzi.android.nim.databinding.NimFragmentGroupListBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ViewModelGroupList
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.entity.arg.ArgGroupList
import com.chuzi.android.shared.ext.showScrollBar
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 群组集合
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_CONTRACT_TEAMGROUPS_LIST)
class FragmentGroupList :
    FragmentBase<NimFragmentGroupListBinding, ViewModelGroupList, ArgGroupList>() {

    override fun setLayoutId(): Int = R.layout.nim_fragment_group_list

    override fun bindVariableId(): Int = BR.viewModel

    override fun initView() {
        super.initView()
        binding.recycler.showScrollBar()
    }

    override fun initData() {
        super.initData()
        viewModel.loadTeamList()
    }

}