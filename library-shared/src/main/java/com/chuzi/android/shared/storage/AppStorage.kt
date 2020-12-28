package com.chuzi.android.shared.storage

import androidx.appcompat.app.AppCompatDelegate
import com.netease.nimlib.sdk.auth.LoginInfo
import com.tencent.mmkv.MMKV
import java.util.*

object AppStorage {

    private const val PREFS_NAME = "app"

    private val prefs: Lazy<MMKV> = lazy {
        MMKV.mmkvWithID(PREFS_NAME)
    }

    var account by StringPreference(prefs, null)

    var token by StringPreference(prefs, null)

    var notifyToggle by BooleanPreference(prefs, true)

    var uiMode by IntPreference(prefs, defaultValue = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

    var language by StringPreference(prefs, defaultValue = null)

    var fontLevel by IntPreference(prefs, defaultValue = 1)

    var isGray by BooleanPreference(prefs, false)

    var isPrivateConfig by BooleanPreference(prefs, false)

    var privateConfigJson by StringPreference(prefs, null)

    fun login(loginInfo: LoginInfo) {
        account = loginInfo.account
        token = loginInfo.token
    }

    fun logout() {
        account = null
        token = null
    }

}