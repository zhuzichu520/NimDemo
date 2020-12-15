package com.chuzi.android.shared.entity.arg

import com.chuzi.android.mvvm.base.BaseArg
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum


/**
 * desc 消息界面参数
 * author: 朱子楚
 * time: 2020/4/5 7:48 PM
 * since: v 1.0.0
 */
data class ArgMessage(
    val contactId: String,
    val sessionType: Int
) : BaseArg() {

    /**
     * 获取SessionType枚举类型
     */
    fun sessionTypeEnum(): SessionTypeEnum {
        return SessionTypeEnum.typeOfValue(sessionType)
    }

}