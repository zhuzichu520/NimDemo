package com.chuzi.android.shared.base

import com.chuzi.android.mvvm.base.BaseArg
import com.chuzi.android.mvvm.base.BaseViewModel
import com.chuzi.android.mvvm.ext.createCommand


/**
 * desc viewModel基类
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
abstract class ViewModelBase<TArg : BaseArg> : BaseViewModel<TArg>() {

    val onBackCommand = createCommand {
        back()
    }

    fun handleThrowable(throwable: Throwable) {
        throwable.printStackTrace()
        toast(throwable.message.toString())
    }

}