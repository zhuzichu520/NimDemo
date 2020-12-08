package com.chuzi.android.shared.route

object RoutePath {

    object Login {
        const val FRAGMENT_LOGIN_MAIN = "/login/fragment/main"
    }

    /**
     * IM 模块
     */
    object Nim {

        const val ACTIVITY_NIM_MAIN = "/nim/activity/main"
        const val FRAGMENT_NIM_MAIN = "/nim/fragment/main"
        const val FRAGMENT_NIM_CONTRACT = "/nim/fragment/contract"
        const val FRAGMENT_NIM_SESSION = "/nim/fragment/session"
        const val FRAGMENT_NIM_SETTING = "/nim/fragment/setting"

        const val ACTIVITY_MESSAGE_MAIN = "/message/activity/main"
        const val FRAGMENT_MESSAGE_P2P = "/message/fragment/p2p"
        const val FRAGMENT_MESSAGE_TEAM = "/message/fragment/team"
        const val FRAGMENT_MESSAGE_EMOTICON = "/message/fragment/emoticon"
        const val FRAGMENT_MESSAGE_OPERATION = "/message/fragment/operation"

    }

}