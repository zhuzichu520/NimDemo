package com.chuzi.android.nim.domain

import com.chuzi.android.mvvm.domain.UseCase
import com.chuzi.android.nim.core.callback.NimRequestCallback
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolImage
import com.chuzi.android.shared.ext.bindToException
import com.chuzi.android.shared.ext.bindToSchedulers
import com.chuzi.android.shared.ext.createFlowable
import com.google.common.base.Optional
import com.netease.nimlib.sdk.msg.MessageBuilder
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.QueryDirectionEnum
import io.reactivex.rxjava3.core.Flowable

/**
 * desc 发送消息
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class UseCaseGetImageAndVideoMessage : UseCase<IMMessage, Flowable<Optional<List<IMMessage>>>>() {

    override fun execute(parameters: IMMessage): Flowable<Optional<List<IMMessage>>> {
        return createFlowable<Optional<List<IMMessage>>> {
            msgService().queryMessageListByTypes(
                listOf(MsgTypeEnum.image, MsgTypeEnum.video),
                MessageBuilder.createEmptyMessage(parameters.sessionId, parameters.sessionType, 0),
                0,
                QueryDirectionEnum.QUERY_OLD,
                Int.MAX_VALUE,
                false
            ).setCallback(NimRequestCallback(this))
        }.flatMap {
            Flowable.fromIterable(it.get())
        }.filter {
            (it.attachment as? ImageAttachment)?.let { attach ->
                !ToolImage.isGif(attach.extension)
            } ?: true
        }.toList().toFlowable().map {
            Optional.fromNullable(it)
        }.bindToSchedulers().bindToException()
    }


}