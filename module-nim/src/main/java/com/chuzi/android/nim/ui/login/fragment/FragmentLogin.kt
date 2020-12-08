package com.chuzi.android.nim.ui.login.fragment

import android.Manifest
import com.alibaba.android.arouter.facade.annotation.Route
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.nim.R
import com.chuzi.android.nim.BR
import com.chuzi.android.nim.databinding.NimFragmentLoginBinding
import com.chuzi.android.nim.ui.login.viewmodel.ViewModelLogin
import com.chuzi.android.shared.base.FragmentBase
import com.chuzi.android.shared.route.RoutePath
import com.chuzi.android.shared.storage.AppStorage
import com.tbruyelle.rxpermissions3.RxPermissions

/**
 * desc 登录页面
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
@Route(path = RoutePath.Login.FRAGMENT_LOGIN_MAIN)
class FragmentLogin : FragmentBase<NimFragmentLoginBinding, ViewModelLogin, ArgDefault>() {

    override fun bindVariableId(): Int = BR.viewModel

    override fun setLayoutId(): Int = R.layout.nim_fragment_login

    override fun initView() {
        super.initView()
        AppStorage.token?.let {
            navigate(RoutePath.Nim.ACTIVITY_NIM_MAIN, isPop = true)
        }
        requestPermission()
    }

    private fun requestPermission() {
        RxPermissions(this).request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ).subscribe {

        }
    }

}

