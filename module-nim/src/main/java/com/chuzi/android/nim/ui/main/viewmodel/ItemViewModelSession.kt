package com.chuzi.android.nim.ui.main.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import com.chuzi.android.mvvm.ext.createCommand
import com.chuzi.android.mvvm.ext.createTypeCommand
import com.chuzi.android.nim.R
import com.chuzi.android.nim.core.attachment.AttachmentSticker
import com.chuzi.android.nim.ext.msgService
import com.chuzi.android.nim.tools.ToolDate
import com.chuzi.android.nim.tools.ToolNimExtension
import com.chuzi.android.nim.tools.ToolTeam
import com.chuzi.android.nim.tools.ToolUserInfo
import com.chuzi.android.nim.ui.session.viewmodel.ViewModelSession
import com.chuzi.android.shared.base.ItemViewModelBase
import com.chuzi.android.shared.ext.toColorByResId
import com.chuzi.android.shared.ext.toStringByResId
import com.chuzi.android.shared.skin.SkinManager
import com.chuzi.android.widget.badge.Badge
import com.chuzi.android.widget.spanly.Spanly
import com.chuzi.android.widget.spanly.color
import com.chuzi.android.widget.spanly.font
import com.netease.nimlib.sdk.msg.attachment.FileAttachment
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.P2P
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum.Team
import com.netease.nimlib.sdk.msg.model.RecentContact
import java.lang.ref.WeakReference

/**
 * desc 消息列表Item的ViewModel
 * author: 朱子楚
 * time: 2020/4/4 11:48 PM
 * since: v 1.0.0
 */
data class ItemViewModelSession(
    val viewModel: ViewModelSession,
    val contact: RecentContact
) : ItemViewModelBase(viewModel) {

    companion object {
        //发送中
        private const val STATE_SEND_LOADING = 0

        //发送失败
        private const val STATE_SEND_FAILED = 1

        //发送完成
        private const val STATE_SEND_NORMAL = 2
    }

    val contactId: String = contact.contactId

    val time = contact.time

    val messageStatus = MutableLiveData<Int>().apply {
        value = when (contact.msgStatus) {
            MsgStatusEnum.fail -> {
                STATE_SEND_FAILED
            }
            MsgStatusEnum.sending -> {
                STATE_SEND_LOADING
            }
            else -> {
                STATE_SEND_NORMAL
            }
        }
    }

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
    val content = MutableLiveData<CharSequence>().apply {
        val draft = ToolNimExtension.getDraft(contact)
        if (draft == null) {
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
        } else {
            value =
                Spanly().append(
                    R.string.draft.toStringByResId(),
                    color(R.color.color_ffc107.toColorByResId())
                ).append(draft)
        }
    }

    /**
     * 会话时间
     */
    val date = MutableLiveData<String>().apply {
        value = ToolDate.getTimeShowString(contact.time, false)
    }

    /**
     * 置顶标记
     */
    val isStick = MutableLiveData<Boolean>().apply {
        value = ToolNimExtension.isStickyTagSet(contact)
    }

    /**
     * 会话未读书
     */
    val number = MutableLiveData<Int>().apply {
        value = contact.unreadCount
    }

    /**
     * 背景颜色
     */
    val background = MutableLiveData<Int>()

    /**
     * 当前View，用来记录长按点击弹出位置
     */
    var viewRef = WeakReference<View>(null)

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
        setItemNormalColor()
    }

    /**
     * 小红点拖动处理
     */
    val onDragStateChangedCommamnd = createTypeCommand<Int> {
        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == it) {
            msgService().clearUnreadCount(contactId, contact.sessionType)
        }
    }

    /**
     * 会话长点击处理
     */
    val onLongClickCommand = createTypeCommand<View> {
        viewRef = WeakReference(it)
        setItemPressedColor()
        viewModel.onItemLongClickEvent.value = this
    }

    /**
     * 会话点击处理
     */
    val onClickCommand = createCommand {
        setItemPressedColor()
        viewModel.onItemClickEvent.value = this
    }

    /**
     * Item正常背景颜色
     */
    fun setItemNormalColor() {
        background.value = if (SkinManager.isDark()) R.color.color_121212 else R.color.color_ffffff
    }

    /**
     * Item按压背景颜色
     */
    private fun setItemPressedColor() {
        background.value = if (SkinManager.isDark()) R.color.color_000000 else R.color.color_f0f2f4
    }

    /**
     * 置顶
     */
    fun addSticky() {
        ToolNimExtension.addStickyTag(contact)
    }

    /**
     * 取消置顶
     */
    fun removeStick() {
        ToolNimExtension.removeStickTag(contact)
    }


    /**
     * 判断是否是通一条消息
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemViewModelSession

        if (contactId != other.contactId) return false

        return true
    }

    override fun hashCode(): Int {
        return contactId.hashCode()
    }

}