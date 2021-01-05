package com.chuzi.android.shared.storage

import com.tencent.mmkv.MMKV

object UserStorage {

    private val prefs: Lazy<MMKV> = lazy {
        MMKV.mmkvWithID(AppStorage.account)
    }

    /**
     * 消息通知声音是否开启
     */
    var ring by BooleanPreference(prefs, false)

    /**
     * 消息通知振动是否开启
     */
    var vibrate by BooleanPreference(prefs, false)


}