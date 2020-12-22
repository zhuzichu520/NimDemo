package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.friendService
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.ext.userService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.chuzi.android.shared.ext.onNextComplete
import com.netease.nimlib.sdk.InvocationFuture
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import io.reactivex.rxjava3.core.Flowable

/**
 * desc
 * author: 朱子楚
 * time: 2020/12/9 10:12 PM
 * since: v 1.0.0
 */
class UseCaseGetFriends :
    UseCase<Unit, Flowable<List<NimUserInfo>>>() {

    override fun execute(parameters: Unit): Flowable<List<NimUserInfo>> {
        return createFlowable<List<NimUserInfo>> {
            onNextComplete(userService().getUserInfoList(friendService().friendAccounts))
        }.bindToSchedulers().bindToException()
    }

}