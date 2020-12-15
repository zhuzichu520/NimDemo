package com.chuzi.android.shared.route

object RoutePath {

    object Login {
        const val FRAGMENT_LOGIN_MAIN = "/login/fragment/main"
        const val ACTIVITY_LOGIN_MAIN = "/login/activity/main"
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
        const val ACTIVITY_NIM_SETTING = "/nim/activity/setting"
        const val FRAGMENT_NIM_SETTING = "/nim/fragment/setting"
        const val FRAGMENT_NIM_SETTING_THEME = "/nim/fragment/setting/theme"
        const val FRAGMENT_NIM_SETTING_LANGUAGE = "/nim/fragment/setting/language"

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
        const val ACTIVITY_NIM_ME_CLEAN_CACHE = "/nim/activity/me/clean_cache"
        const val FRAGMENT_NIM_ME_CLEAN_CACHE = "/nim/fragment/me/clean_cache"
        const val ACTIVITY_NIM_ME_CLEAN_SESSION = "/nim/activity/me/clean_session"
        const val FRAGMENT_NIM_ME_CLEAN_SESSION = "/nim/fragment/me/clean_session"
    }


    /**
     * Media 模块
     */
    object Media {
        const val FRAGMENT_MEDIA_STYLE = "/media/fragment/style"
    }

}