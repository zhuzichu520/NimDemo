package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.netease.nimlib.sdk.InvocationFuture
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum

/**
 * desc 获取最近会话列表数据
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class UseCaseGetMessageList :
    UseCase<UseCaseGetMessageList.Parameters, InvocationFuture<List<IMMessage>>>() {

    override fun execute(parameters: Parameters): InvocationFuture<List<IMMessage>> {
        return msgService().queryMessageListEx(
            parameters.anchor,
            QueryDirectionEnum.QUERY_OLD,
            parameters.pageSize,
            true
        ).apply {
            setCallback(parameters.callback)
        }

    }

    data class Parameters(
        val anchor: IMMessage,
        val pageSize: Int,
        val callback: NimRequestCallback<List<IMMessage>>
    )

}