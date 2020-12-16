package com.chuzi.android.nim.core.event

import com.chuzi.android.nim.ext.authServiceObserver
import com.chuzi.android.nim.ext.friendServiceObserve
import com.chuzi.android.nim.ext.msgServiceObserve
import com.chuzi.android.nim.ext.userServiceObserve
import com.netease.nimlib.sdk.StatusCode
import com.netease.nimlib.sdk.auth.OnlineClient
import com.netease.nimlib.sdk.friend.model.FriendChangedNotify
import com.netease.nimlib.sdk.msg.model.AttachmentProgress
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.msg.model.RecentContact
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo
import com.chuzi.android.shared.bus.RxBus
import com.netease.nimlib.sdk.Observer

object NimEventManager {

    /**
     * 联系人监听
     */
    private val observerRecentContact =
        Observer { list: List<RecentContact>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnRecentContactEvent(list))
            }
        }

    /**
     * 用户在线监听
     */
    private val observeOnlineStatus =
        Observer { statusCode: StatusCode? ->
            statusCode?.let {
                RxBus.post(NimEvent.OnLineStatusEvent(it))
            }
        }

    /**
     * 多端登录监听
     */
    private val observeOtherClients =
        Observer { onlineClients: List<OnlineClient>? ->
            RxBus.post(NimEvent.OnOtherClientsEvent(onlineClients))
        }

    /**
     * 消息状态监听
     */
    private val observeMessageStatus =
        Observer { message: IMMessage? ->
            message?.let {
                RxBus.post(NimEvent.OnMessageStatusEvent(it))
            }
        }

    /**
     * 消息接受监听
     */
    private val observeReceiveMessage =
        Observer { list: List<IMMessage>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnReceiveMessageEvent(list))
            }
        }

    /**
     * 附件下载监听
     */
    private val observerAttachmentProgress =
        Observer { attachmentProgress: AttachmentProgress? ->
            attachmentProgress?.let {
                RxBus.post(NimEvent.OnAttachmentProgressEvent(it))
            }
        }

    /**
     * 监听用户资料变更  不会实时监听
     * 用户资料除自己之外，不保证其他用户资料实时更新。其他用户数据更新时机为：
     * 1. 调用 fetchUserInfo 方法刷新用户
     * 2. 收到此用户发来消息（如果消息发送者有资料变更，SDK 会负责更新并通知）
     * 3. 程序再次启动，此时会同步好友信息
     */
    private val observerUserInfoUpdate =
        Observer { list: List<NimUserInfo>? ->
            if (!list.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnUserInfoUpdateEvent(list))
            }
        }

    /**
     * 第三方 APP 应在 APP 启动后监听好友关系的变化，当主动添加好友成功、被添加为好友、
     * 主动删除好友成功、被对方解好友关系、好友关系更新、登录同步好友关系数据时都会收到通知。
     */
    private val observerFriendChangedNotify =
        Observer { friendChangedNotify: FriendChangedNotify ->
            val addedOrUpdatedFriends = friendChangedNotify.addedOrUpdatedFriends
            val deletedFriends = friendChangedNotify.deletedFriends
            if (!addedOrUpdatedFriends.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnAddedOrUpdatedFriendsEvent(addedOrUpdatedFriends))
            }
            if (!deletedFriends.isNullOrEmpty()) {
                RxBus.post(NimEvent.OnDeletedFriendsEvent(deletedFriends))
            }
        } as Observer<FriendChangedNotify>


    fun registerObserves(register: Boolean) {

        msgServiceObserve().observeAttachmentProgress(observerAttachmentProgress, register)

        msgServiceObserve().observeReceiveMessage(observeReceiveMessage, register)

        msgServiceObserve().observeMsgStatus(observeMessageStatus, register)

        msgServiceObserve().observeRecentContact(observerRecentContact, register)

        userServiceObserve().observeUserInfoUpdate(observerUserInfoUpdate, register)

        friendServiceObserve().observeFriendChangedNotify(observerFriendChangedNotify, register)

    }

    fun registObserveOtherClients(register: Boolean) {
        authServiceObserver().observeOtherClients(observeOtherClients, register)
    }

    fun registObserveOnlineStatus(register: Boolean) {
        authServiceObserver().observeOnlineStatus(observeOnlineStatus, register)
    }

}