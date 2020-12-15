package com.chuzi.android.nim.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.chuzi.android.libs.internal.MainHandler
import com.chuzi.android.libs.tool.md5
import com.chuzi.android.mvvm.base.ArgDefault
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.api.NimCallBack
import com.chuzi.android.nim.domain.UseCaseLogin
import com.chuzi.android.shared.base.ViewModelBase
import com.chuzi.android.shared.route.RoutePath

/**
 * desc 登录ViewModel
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class ViewModelLogin : ViewModelBase<ArgDefault>() {

    private val useCaseLogin by lazy {
        UseCaseLogin()
    }

    val username = MutableLiveData<String>("a7711451")

    val password = MutableLiveData<String>("7711451")


    val onClickLoginCommand = createCommand {
        val account = username.value
        val password = password.value
        if (account.isNullOrEmpty()) {
            toast("账号不能为空")
            return@createCommand
        }
        if (password.isNullOrEmpty()) {
            toast("密码不能为空")
            return@createCommand
        }
        showLoading()
        AppFactorySDK.login(account, md5(password), object :
            NimCallBack<String> {
            override fun onSuccess(result: String?) {
                MainHandler.postDelayed {
                    navigate(RoutePath.Nim.ACTIVITY_NIM_MAIN, isPop = true)
                }
                hideLoading()
            }

            override fun onFail(code: Int, message: String?) {
                hideLoading()
            }
        })

    }

}
