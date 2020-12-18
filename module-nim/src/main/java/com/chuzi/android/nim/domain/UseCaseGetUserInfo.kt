package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.userService
import com.netease.nimlib.sdk.InvocationFuture
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:12 PM
 * since: v 1.0.0
 */
class UseCaseGetUserInfo :
    UseCase<UseCaseGetUserInfo.Parameters, InvocationFuture<List<NimUserInfo>>>() {

    override fun execute(parameters: Parameters): InvocationFuture<List<NimUserInfo>> {
        return userService().fetchUserInfo(parameters.accounts).apply {
            setCallback(parameters.callback)
        }
    }

    data class Parameters(
        val accounts: List<String?>,
        val callback: NimRequestCallback<List<NimUserInfo>>
    )

}