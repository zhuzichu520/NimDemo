package com.chuzi.android.nim.core.event

import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.friend.model.Friend
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo

/**
 * desc IM事件
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
class NimEvent {
    /**
     * 最近会话事件
     */
    data class OnRecentContactEvent(val list: List<RecentContact>)

    /**
     * 用户在线监听
     */
    data class OnLineStatusEvent(val statusCode: StatusCode)

    /**
     * 多端登录监听
     */
    data class OnOtherClientsEvent(val list: List<OnlineClient>)

    /**
     * 消息状态监听
     */
    data class OnMessageStatusEvent(val message: IMMessage)

    /**
     * 消息接受监听
     */
    data class OnReceiveMessageEvent(val list: List<IMMessage>)

    /**
     * 消息附件进度监听
     */
    data class OnAttachmentProgressEvent(val attachment: AttachmentProgress)

    /**
     * 用户资料更新监听
     */
    data class OnUserInfoUpdateEvent(val list: List<NimUserInfo>)

    /**
     * 好友关系监听
     */
    data class OnAddedOrUpdatedFriendsEvent(val list: List<Friend>)

    /**
     * 好友删除监听
     */
    data class OnDeletedFriendsEvent(val list: List<String>)

}