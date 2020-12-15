package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.google.common.base.Optional
import com.netease.nimlib.sdk.msg.model.IMMessage
import io.reactivex.rxjava3.core.Flowable

/**
 * desc 获取IM用户资料详情
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseDowloadAttachment :
    UseCase<UseCaseDowloadAttachment.Parameters, Flowable<Optional<Void>>>() {

    override fun execute(parameters: Parameters): Flowable<Optional<Void>> {
        return createFlowable<Optional<Void>> {
            msgService().downloadAttachment(parameters.message, parameters.thumb)
                .setCallback(NimRequestCallback(this))
        }.bindToSchedulers().bindToException()
    }

    data class Parameters(
        val message: IMMessage,
        val thumb: Boolean
    )
}