package com.chuzi.android.shared.route

object RoutePath {

    const val DIALOG_PERMISSIONS_MAIN = "/permissions/dialog/main"

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
        const val ACTIVITY_NIM_CONTRACT_FRIENDS = "/nim/activity/contract/friends"
        const val FRAGMENT_NIM_CONTRACT_FRIENDS = "/nim/fragment/contract/friends"

        /**
         * 群组
         */
        const val ACTIVITY_NIM_CONTRACT_TEAMGROUPS = "/nim/activity/contract/team_groups"
        const val FRAGMENT_NIM_CONTRACT_TEAMGROUPS = "/nim/fragment/contract/team_groups"
        const val FRAGMENT_NIM_CONTRACT_TEAMGROUPS_LIST = "/nim/fragment/contract/team_groups/list"

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
        const val FRAGMENT_NIM_SETTING_FONT = "/nim/fragment/setting/font"
        const val FRAGMENT_NIM_SETTING_NOTIFICATION = "/nim/fragment/setting/notification"
        const val FRAGMENT_NIM_SETTING_LANGUAGE = "/nim/fragment/setting/language"

        /**
         * Message
         */
        const val ACTIVITY_NIM_MESSAGE = "/nim/activity/message"
        const val FRAGMENT_NIM_MESSAGE_P2P = "/nim/fragment/message/p2p"
        const val FRAGMENT_NIM_MESSAGE_EMOTICON = "/nim/fragment/message/emoticon"
        const val FRAGMENT_NIM_MESSAGE_EMOTICON_EMOJI = "/nim/fragment/message/emoticon/emoji"
        const val FRAGMENT_NIM_MESSAGE_EMOTICON_STICKER = "/nim/fragment/message/emoticon/sticker"
        const val FRAGMENT_NIM_MESSAGE_OPERATION = "/nim/fragment/message/operation"

        const val ACTIVITY_NIM_MESSAGE_SETTING = "/nim/activity/message/setting"
        const val FRAGMENT_NIM_MESSAGE_SETTING = "/nim/fragment/message/setting"


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
     * 地图模块
     */
    object Map {
        const val ACTIVITY_MAP_LOCATION = "/map/activity/location"
        const val FRAGMENT_MAP_LOCATION = "/map/fragment/location"
    }


    /**
     * Media 模块
     */
    object Media {
        const val FRAGMENT_MEDIA_STYLE = "/media/fragment/style"
    }

}