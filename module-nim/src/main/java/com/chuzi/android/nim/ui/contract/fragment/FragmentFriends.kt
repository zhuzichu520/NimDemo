package com.chuzi.android.nim.ui.contract.fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentFriendsBinding
import com.chuzi.android.nim.ui.contract.viewmodel.ItemViewModelFriendIndex
import com.chuzi.android.nim.ui.contract.viewmodel.ViewModelFriends
import com.chuzi.android.nim.view.QuickIndexBar
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 我的好友
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Nim.FRAGMENT_NIM_CONTRACT_FRIENDS)
class FragmentFriends : FragmentBase<NimFragmentFriendsBinding, ViewModelFriends, ArgDefault>() {

    private val layoutManager: LinearLayoutManager by lazy {
        binding.recycler.layoutManager as LinearLayoutManager
    }

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_friends

    override fun initListener() {
        super.initListener()
        binding.viewIndex.setOnTouchingLetterChangedListener(object :
            QuickIndexBar.OnTouchingLetterChangedListener {

            override fun onHit(letter: String?) {
                updateShowLetter(letter)
            }

            override fun onCancel() {
                hideLetter()
            }

        })
    }

    override fun initData() {
        super.initData()
        viewModel.loadFriends()
    }

    private fun updateShowLetter(letter: String?) {
        viewModel.letter.value = letter
        viewModel.showLetter.value = true
        viewModel.getIndexByLetter(letter)?.let {
            layoutManager.scrollToPositionWithOffset(it, 0)
        }
    }

    private fun hideLetter() {
        viewModel.showLetter.value = false
    }

}