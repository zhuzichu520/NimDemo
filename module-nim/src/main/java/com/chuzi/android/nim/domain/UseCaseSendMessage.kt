package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.netease.nimlib.sdk.InvocationFuture
import com.netease.nimlib.sdk.msg.model.IMMessage

/**
 * desc 发送消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseSendMessage : UseCase<UseCaseSendMessage.Parameters, InvocationFuture<Void>>() {

    override fun execute(parameters: Parameters): InvocationFuture<Void> {
        return msgService().sendMessage(parameters.message, parameters.resend).apply {
            setCallback(parameters.callback)
        }
    }

    data class Parameters(
        val message: IMMessage,
        val resend: Boolean,
        val callback: NimRequestCallback<Void>
    )

}