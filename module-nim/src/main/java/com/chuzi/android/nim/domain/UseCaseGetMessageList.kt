package com.chuzi.android.nim.domain

import com.google.common.base.Optional
import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import io.reactivex.rxjava3.core.Flowable

/**
 * desc 获取最近会话列表数据
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
class UseCaseGetMessageList :
    UseCase<UseCaseGetMessageList.Parameters, Flowable<Optional<List<IMMessage>>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<List<IMMessage>>> {
        return createFlowable<Optional<List<IMMessage>>> {
            msgService().queryMessageListEx(
                parameters.anchor,
                QueryDirectionEnum.QUERY_OLD,
                parameters.pageSize,
                true
            ).setCallback(NimRequestCallback(this))
        }.bindToSchedulers().bindToException()
    }

    data class Parameters(
        val anchor: IMMessage,
        val pageSize: Int
    )

}