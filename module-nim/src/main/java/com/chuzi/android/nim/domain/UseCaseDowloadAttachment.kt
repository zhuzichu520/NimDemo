package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.netease.nimlib.sdk.AbortableFuture
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 下载附件
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseDowloadAttachment :
    UseCase<UseCaseDowloadAttachment.Parameters, AbortableFuture<Void>>() {

    override fun execute(parameters: Parameters): AbortableFuture<Void> {
        return msgService().downloadAttachment(parameters.message, parameters.thumb)
            .apply {
                setCallback(parameters.callback)
            }
    }

    data class Parameters(
        val message: IMMessage,
        val thumb: Boolean,
        val callback: NimRequestCallback<Void>
    )
}