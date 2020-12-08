package com.chuzi.android.nim.domain

import com.google.common.base.Optional
import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.authService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.auth.AuthService
import com.netease.nimlib.sdk.auth.LoginInfo
import io.reactivex.rxjava3.core.Flowable

/**
 * desc 登录UseCase
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseLogin : UseCase<LoginInfo, Flowable<Optional<LoginInfo>>>() {

    override fun execute(parameters: LoginInfo): Flowable<Optional<LoginInfo>> {
        return createFlowable<Optional<LoginInfo>> {
            authService().login(parameters).setCallback(
                NimRequestCallback(this)
            )
        }.bindToSchedulers().bindToException()
    }

}