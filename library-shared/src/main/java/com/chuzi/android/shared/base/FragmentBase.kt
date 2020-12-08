package com.chuzi.android.shared.base

import android.content.Context
import androidx.databinding.ViewDataBinding
import com.chuzi.android.mvvm.base.BaseArg
import com.chuzi.android.mvvm.base.BaseFragment
import com.chuzi.android.mvvm.base.BaseViewModel
import com.qmuiteam.qmui.arch.SwipeBackLayout
import com.qmuiteam.qmui.util.QMUIDisplayHelper

abstract class FragmentBase<TBinding : ViewDataBinding, TViewModel : BaseViewModel<TArg>, TArg : BaseArg> :
    BaseFragment<TBinding, TViewModel, TArg>() {

    override fun backViewInitOffset(context: Context?, dragDirection: Int, moveEdge: Int): Int {
        return if (moveEdge == SwipeBackLayout.EDGE_TOP || moveEdge == SwipeBackLayout.EDGE_BOTTOM) {
            0
        } else QMUIDisplayHelper.dp2px(context, 100)
    }

}
