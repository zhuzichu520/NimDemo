package com.chuzi.android.nim.ui.me.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.api.AppFactorySDK
import com.chuzi.android.nim.core.attachment.AttachmentSticker
import com.chuzi.android.nim.tools.ToolDate
import com.chuzi.android.nim.tools.ToolTeam
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.shared.base.ItemViewModelBase
import com.netease.nimlib.sdk.msg.attachment.FileAttachment
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.Team
import com.netease.nimlib.sdk.msg.model.RecentContact

/**
 * desc 消息列表Item的ViewModel
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
data class ItemViewModelCacheSession(
    val viewModel: ViewModelCleanSession,
    val contact: RecentContact
) : ItemViewModelBase(viewModel) {

    val contactId: String = contact.contactId

    val time = contact.time

    val isSelected = MutableLiveData(false)

    val isGray: LiveData<Boolean> = Transformations.map(AppFactorySDK.isGrayImage) { it }

    /**
     * 头像
     */
    val avatar = MutableLiveData<Any>()

    /**
     * 会话标题
     */
    val name = MutableLiveData<String>()

    /**
     * 头像占位图
     */
    val placeholder = MutableLiveData<Int>()

    /**
     * todo 可优化
     * 会话内容
     */
    val content = MutableLiveData<String>().apply {
        val attachment = contact.attachment
        value = when (attachment) {
            is AttachmentSticker -> {
                "[贴图]"
            }
            is ImageAttachment -> {
                "[图片]"
            }
            is FileAttachment -> {
                "[文件]"
            }
            else -> {
                contact.content ?: ""
            }
        }
    }

    /**
     * 会话时间
     */
    val date = MutableLiveData<String>().apply {
        value = ToolDate.getTimeShowString(contact.time, false)
    }


    /**
     * 背景颜色
     */
    val background = MutableLiveData<Int>()


    /**
     * 初始化数据
     */
    init {
        when (contact.sessionType) {
            P2P -> {
                val userInfo = ToolUserInfo.getUserInfo(contactId)
                name.value = ToolUserInfo.getUserDisplayName(contactId)
                avatar.value = userInfo?.avatar
                placeholder.value = R.mipmap.nim_avatar_default
            }
            Team -> {
                val team = ToolTeam.getTeam(contactId)
                name.value = team.name
                avatar.value = team.icon
                placeholder.value = R.mipmap.nim_avatar_group
            }
            else -> {
            }
        }
    }


    /**
     * 会话点击处理
     */
    val onClickCommand = createCommand {
        isSelected.value = isSelected.value != true
        viewModel.checkAll()
    }


    /**
     * 判断是否是通一条消息
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemViewModelCacheSession

        if (contactId != other.contactId) return false

        return true
    }

    override fun hashCode(): Int {
        return contactId.hashCode()
    }

}