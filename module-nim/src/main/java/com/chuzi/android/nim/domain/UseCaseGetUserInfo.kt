package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.userService
import com.chuzi.android.shared.ext.createFlowable
import com.google.common.base.Optional
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import io.reactivex.rxjava3.core.Flowable

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:12 PM
 * since: v 1.0.0
 */
class UseCaseGetUserInfo : UseCase<String, Flowable<Optional<NimUserInfo>>>() {

    override fun execute(parameters: String): Flowable<Optional<NimUserInfo>> {
        return createFlowable<Optional<List<NimUserInfo>>> {
            val userInfo = userService().getUserInfo(parameters)
            userInfo?.let {
                onNext(Optional.fromNullable(listOf<NimUserInfo>(userInfo)))
                onComplete()
            } ?: let {
                userService().fetchUserInfo(listOf(parameters)).setCallback(NimRequestCallback(it))
            }
        }.map {
            Optional.fromNullable(it.get()[0])
        }
    }

}