package com.chuzi.android.shared.route

object RoutePath {

    object Login {
        const val FRAGMENT_LOGIN_MAIN = "/login/fragment/main"
    }

    /**
     * IM 模块
     */
    object Nim {

        /**
         * NIM
         */
        const val ACTIVITY_NIM_MAIN = "/nim/activity/main"
        const val FRAGMENT_NIM_MAIN = "/nim/fragment/main"

        /**
         * 联系人
         */
        const val FRAGMENT_NIM_CONTRACT = "/nim/fragment/contract"

        /**
         * 会话
         */
        const val FRAGMENT_NIM_SESSION = "/nim/fragment/session"

        /**
         * 设置
         */
        const val FRAGMENT_NIM_SETTING = "/nim/fragment/setting"

        /**
         * Message
         */
        const val ACTIVITY_NIM_MESSAGE = "/nim/activity/message"
        const val FRAGMENT_NIM_MESSAGE_P2P = "/nim/fragment/message/p2p"
        const val FRAGMENT_NIM_MESSAGE_EMOTICON = "/nim/fragment/message/emoticon"
        const val FRAGMENT_NIM_MESSAGE_OPERATION = "/nim/fragment/message/operation"

        /**
         * 我的
         */
        const val FRAGMENT_NIM_ME = "/nim/fragment/me"
    }

}