package com.chuzi.android.nim.tools

import android.text.TextUtils
import com.chuzi.android.nim.R
import com.chuzi.android.nim.tools.ToolTeam.getTeamById
import com.chuzi.android.nim.tools.ToolTeam.getTeamMemberDisplayNameYou
import com.chuzi.android.shared.global.AppGlobal.context
import com.netease.nimlib.sdk.msg.attachment.NotificationAttachment
import com.netease.nimlib.sdk.msg.constant.NotificationType
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum
import com.netease.nimlib.sdk.msg.model.IMMessage
import com.netease.nimlib.sdk.team.constant.TeamAllMuteModeEnum
import com.netease.nimlib.sdk.team.constant.TeamFieldEnum
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum
import com.netease.nimlib.sdk.team.constant.VerifyTypeEnum
import com.netease.nimlib.sdk.team.model.MemberChangeAttachment
import com.netease.nimlib.sdk.team.model.MuteMemberAttachment
import com.netease.nimlib.sdk.team.model.UpdateTeamAttachment

/**
 * 系统消息描述文本构造器。主要是将各个系统消息转换为显示的文本内容。<br></br>
 * Created by huangjun on 2015/3/11.
 */
object ToolTeamNotification {
    private val teamId = ThreadLocal<String?>()
    fun getMsgShowText(message: IMMessage): String {
        var content = ""
        val messageTip = message.msgType.sendMessageTip
        content += if (messageTip.isNotEmpty()) {
            "[$messageTip]"
        } else {
            if (message.sessionType == SessionTypeEnum.Team && message.attachment != null) {
                getTeamNotificationText(message, message.sessionId)
            } else {
                message.content
            }
        }
        return content
    }

    fun getTeamNotificationText(message: IMMessage, tid: String?): String {
        return getTeamNotificationText(
            message.sessionId,
            message.fromAccount,
            message.attachment as NotificationAttachment
        )
    }

    fun getTeamNotificationText(
        tid: String?,
        fromAccount: String?,
        attachment: NotificationAttachment
    ): String {
        teamId.set(tid)
        val text = buildNotification(tid, fromAccount, attachment)
        teamId.set(null)
        return text
    }

    fun buildNotification(
        tid: String?,
        fromAccount: String?,
        attachment: NotificationAttachment
    ): String {
        return when (attachment.type) {
            NotificationType.InviteMember, NotificationType.SUPER_TEAM_INVITE -> buildInviteMemberNotification(
                attachment as MemberChangeAttachment,
                fromAccount
            )
            NotificationType.KickMember, NotificationType.SUPER_TEAM_KICK -> buildKickMemberNotification(
                attachment as MemberChangeAttachment
            )
            NotificationType.LeaveTeam, NotificationType.SUPER_TEAM_LEAVE -> buildLeaveTeamNotification(
                fromAccount
            )
            NotificationType.DismissTeam, NotificationType.SUPER_TEAM_DISMISS -> buildDismissTeamNotification(
                fromAccount
            )
            NotificationType.UpdateTeam, NotificationType.SUPER_TEAM_UPDATE_T_INFO -> buildUpdateTeamNotification(
                tid,
                fromAccount,
                attachment as UpdateTeamAttachment
            )
            NotificationType.PassTeamApply, NotificationType.SUPER_TEAM_APPLY_PASS -> buildManagerPassTeamApplyNotification(
                attachment as MemberChangeAttachment
            )
            NotificationType.TransferOwner, NotificationType.SUPER_TEAM_CHANGE_OWNER -> buildTransferOwnerNotification(
                fromAccount,
                attachment as MemberChangeAttachment
            )
            NotificationType.AddTeamManager, NotificationType.SUPER_TEAM_ADD_MANAGER -> buildAddTeamManagerNotification(
                attachment as MemberChangeAttachment
            )
            NotificationType.RemoveTeamManager, NotificationType.SUPER_TEAM_REMOVE_MANAGER -> buildRemoveTeamManagerNotification(
                attachment as MemberChangeAttachment
            )
            NotificationType.AcceptInvite, NotificationType.SUPER_TEAM_INVITE_ACCEPT -> buildAcceptInviteNotification(
                fromAccount,
                attachment as MemberChangeAttachment
            )
            NotificationType.MuteTeamMember, NotificationType.SUPER_TEAM_MUTE_TLIST -> buildMuteTeamNotification(
                attachment as MuteMemberAttachment
            )
            else -> getTeamMemberDisplayName(fromAccount) + ": unknown message"
        }
    }

    private fun getTeamMemberDisplayName(account: String?): String {
        return getTeamMemberDisplayNameYou(teamId.get(), account!!)
    }

    private fun buildMemberListString(members: List<String>, fromAccount: String?): String {
        val sb = StringBuilder()
        for (account in members) {
            if (!TextUtils.isEmpty(fromAccount) && fromAccount == account) {
                continue
            }
            sb.append(getTeamMemberDisplayName(account))
            sb.append(",")
        }
        sb.deleteCharAt(sb.length - 1)
        return sb.toString()
    }

    private fun buildInviteMemberNotification(
        a: MemberChangeAttachment,
        fromAccount: String?
    ): String {
        val sb = StringBuilder()
        val selfName = getTeamMemberDisplayName(fromAccount)
        sb.append(selfName)
        sb.append("邀请 ")
        sb.append(buildMemberListString(a.targets, fromAccount))
        val team = getTeamById(teamId.get())
        if (team == null || team.type == TeamTypeEnum.Advanced) {
            sb.append(" 加入群")
        } else {
            sb.append(" 加入讨论组")
        }
        return sb.toString()
    }

    private fun buildKickMemberNotification(a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append(buildMemberListString(a.targets, null))
        val team = getTeamById(teamId.get())
        if (team == null || team.type == TeamTypeEnum.Advanced) {
            sb.append(" 已被移出群")
        } else {
            sb.append(" 已被移出讨论组")
        }
        return sb.toString()
    }

    private fun buildLeaveTeamNotification(fromAccount: String?): String {
        val tip: String
        val team = getTeamById(teamId.get())
        tip = if (team == null || team.type == TeamTypeEnum.Advanced) {
            " 离开了群"
        } else {
            " 离开了讨论组"
        }
        return getTeamMemberDisplayName(fromAccount) + tip
    }

    private fun buildDismissTeamNotification(fromAccount: String?): String {
        return getTeamMemberDisplayName(fromAccount) + " 解散了群"
    }

    private fun buildUpdateTeamNotification(
        tid: String?,
        account: String?,
        a: UpdateTeamAttachment
    ): String {
        val sb = StringBuilder()
        for ((key, value) in a.updatedFields) {
            if (key == TeamFieldEnum.Name) {
                sb.append("名称被更新为 $value")
            } else if (key == TeamFieldEnum.Introduce) {
                sb.append("群介绍被更新为 $value")
            } else if (key == TeamFieldEnum.Announcement) {
                sb.append(getTeamMemberDisplayNameYou(tid, account!!) + " 修改了群公告")
            } else if (key == TeamFieldEnum.VerifyType) {
                val type = value as VerifyTypeEnum
                val authen = "群身份验证权限更新为"
                if (type == VerifyTypeEnum.Free) {
                    sb.append(authen + context.getString(R.string.team_allow_anyone_join))
                } else if (type == VerifyTypeEnum.Apply) {
                    sb.append(authen + context.getString(R.string.team_need_authentication))
                } else {
                    sb.append(authen + context.getString(R.string.team_not_allow_anyone_join))
                }
            } else if (key == TeamFieldEnum.Extension) {
                sb.append("群扩展字段被更新为 $value")
            } else if (key == TeamFieldEnum.Ext_Server_Only) {
                sb.append("群扩展字段(服务器)被更新为 $value")
            } else if (key == TeamFieldEnum.ICON) {
                sb.append("群头像已更新")
            } else if (key == TeamFieldEnum.InviteMode) {
                sb.append("群邀请他人权限被更新为 $value")
            } else if (key == TeamFieldEnum.TeamUpdateMode) {
                sb.append("群资料修改权限被更新为 $value")
            } else if (key == TeamFieldEnum.BeInviteMode) {
                sb.append("群被邀请人身份验证权限被更新为 $value")
            } else if (key == TeamFieldEnum.TeamExtensionUpdateMode) {
                sb.append("群扩展字段修改权限被更新为 $value")
            } else if (key == TeamFieldEnum.AllMute) {
                val teamAllMuteModeEnum = value as TeamAllMuteModeEnum
                if (teamAllMuteModeEnum == TeamAllMuteModeEnum.Cancel) {
                    sb.append("取消群全员禁言")
                } else {
                    sb.append("群全员禁言")
                }
            } else {
                sb.append("群" + key + "被更新为 " + value)
            }
            sb.append("\r\n")
        }
        return if (sb.length < 2) {
            "未知通知"
        } else sb.delete(sb.length - 2, sb.length).toString()
    }

    private fun buildManagerPassTeamApplyNotification(a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append("管理员通过用户 ")
        sb.append(buildMemberListString(a.targets, null))
        sb.append(" 的入群申请")
        return sb.toString()
    }

    private fun buildTransferOwnerNotification(from: String?, a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append(getTeamMemberDisplayName(from))
        sb.append(" 将群转移给 ")
        sb.append(buildMemberListString(a.targets, null))
        return sb.toString()
    }

    private fun buildAddTeamManagerNotification(a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append(buildMemberListString(a.targets, null))
        sb.append(" 被任命为管理员")
        return sb.toString()
    }

    private fun buildRemoveTeamManagerNotification(a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append(buildMemberListString(a.targets, null))
        sb.append(" 被撤销管理员身份")
        return sb.toString()
    }

    private fun buildAcceptInviteNotification(from: String?, a: MemberChangeAttachment): String {
        val sb = StringBuilder()
        sb.append(getTeamMemberDisplayName(from))
        sb.append(" 接受了 ").append(buildMemberListString(a.targets, null)).append(" 的入群邀请")
        return sb.toString()
    }

    private fun buildMuteTeamNotification(a: MuteMemberAttachment): String {
        val sb = StringBuilder()
        sb.append(buildMemberListString(a.targets, null))
        sb.append("被管理员")
        sb.append(if (a.isMute) "禁言" else "解除禁言")
        return sb.toString()
    }
}