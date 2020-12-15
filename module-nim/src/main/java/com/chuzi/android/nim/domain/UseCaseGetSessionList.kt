package com.chuzi.android.nim.domain

import com.google.common.base.Optional
import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.netease.nimlib.sdk.msg.model.RecentContact
import io.reactivex.rxjava3.core.Flowable

/**
 * desc 获取最近会话列表数据
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class UseCaseGetSessionList : UseCase<Unit, Flowable<Optional<List<RecentContact>>>>() {

    override fun execute(parameters: Unit): Flowable<Optional<List<RecentContact>>> {
        return createFlowable {
            msgService().queryRecentContacts().setCallback(
                NimRequestCallback(this)
            )
        }
    }

}