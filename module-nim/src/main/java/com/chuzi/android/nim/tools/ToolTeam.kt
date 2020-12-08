package com.chuzi.android.nim.tools

import com.chuzi.android.nim.ext.teamService
import com.chuzi.android.shared.storage.AppStorage
import com.netease.nimlib.sdk.NIMClient
import com.netease.nimlib.sdk.team.TeamService
import com.netease.nimlib.sdk.team.constant.TeamTypeEnum
import com.netease.nimlib.sdk.team.model.Team

object ToolTeam {

    fun getTeam(contactId: String): Team {
        return teamService().queryTeamBlock(contactId)
    }


    /**
     * 获取显示名称。用户本人显示“你”
     *
     * @param tid
     * @param account
     * @return
     */
    fun getTeamMemberDisplayNameYou(
        tid: String?,
        account: String
    ): String {
        return if (account == AppStorage.account) {
            "你"
        } else getDisplayNameWithoutMe(
            tid,
            account
        )
    }

    private fun getDisplayNameWithoutMe(tid: String?, account: String): String {

        val alias: String? = ToolUserInfo.getAlias(account)
        if (!alias.isNullOrEmpty()) {
            return alias
        }
        if (tid == null)
            return ToolUserInfo.getUserName(account).toString()
        val memberNick: String? = getTeamNick(tid, account)
        if (!memberNick.isNullOrEmpty()) {
            return memberNick
        }
        return ToolUserInfo.getUserName(account).toString()
    }

    private fun getTeamNick(tid: String, account: String): String? {
        val team = teamService().queryTeamBlock(tid) ?: return null
        if (team.type != TeamTypeEnum.Advanced) {
            return null
        }
        val teamMember = teamService().queryTeamMemberBlock(tid, account) ?: return null
        return teamMember.teamNick
    }


    fun getTeamById(tid: String?): Team? {
        if (tid == null)
            return null
        return teamService().queryTeamBlock(tid) ?: return null
    }

}